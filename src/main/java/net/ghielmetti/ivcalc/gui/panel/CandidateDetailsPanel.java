package net.ghielmetti.ivcalc.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.gui.model.CandidateDetailsModelIfc;
import net.ghielmetti.ivcalc.gui.renderer.CandidateDetailRenderer;
import net.ghielmetti.utilities.Translations;

/**
 * Displays all the possible details of a candidate.
 *
 * @author Leopoldo Ghielmetti
 */
public class CandidateDetailsPanel extends JPanel implements Observer {
  private CandidateDetailsModelIfc model;
  private CandidateList            candidate;
  private JList<CandidateDetail>   candidateDetails;
  private JLabel                   bestCandidate;
  private JComboBox<String>        comboBoxNames;
  private List<String>             names;

  /**
   * Creates a new panel.
   *
   * @param inModel The model for this panel.
   */
  public CandidateDetailsPanel(final CandidateDetailsModelIfc inModel) {
    super(new BorderLayout());

    names = new ArrayList<>();
    model = inModel;
    model.addObserver(this);

    initialize();
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    if (CandidateDetailsModelIfc.OBSERVE_SELECTED_CANDIDATE.equals(inArg)) {
      candidate = model.getSelectedCandidate();

      if (candidate == null) {
        comboBoxNames.setModel(new DefaultComboBoxModel<>(new String[0]));
        names = new ArrayList<>();
        bestCandidate.setText(" ");
        model.setSelectedDetails(null);
      } else {
        names = model.getSavedCandidatesNames(candidate.getPokemon(), candidate.getLimit());
        comboBoxNames.setModel(new DefaultComboBoxModel<>(names.toArray(new String[0])));
        comboBoxNames.setSelectedItem(null);
        bestCandidate.setText(Translations.translate("label.bestCandidate40", Integer.valueOf(candidate.getMaxCP())));
      }

      addCandidates();

      SwingUtilities.invokeLater(() -> candidateDetails.updateUI());
    }
  }

  private void addCandidates() {
    DefaultListModel<CandidateDetail> newModel = new DefaultListModel<>();

    if (candidate != null) {
      for (Integer level : candidate.getPossibleLevels()) {
        for (IVLevel ivLevel : candidate.getIVLevels(level)) {
          boolean isBad = !model.isGood(candidate.getPokemon().getName(), ivLevel);
          ImmutablePair<Integer, Integer> minMaxCP = candidate.getPokemon().getMinMaxCP(level.intValue());
          newModel.addElement(new CandidateDetail(isBad, minMaxCP, level, ivLevel, model.getCP()));
        }
      }
    }

    candidateDetails.setModel(newModel);

    SwingUtilities.invokeLater(() -> {
      candidateDetails.setSelectedIndex(0);
      candidateDetails.ensureIndexIsVisible(0);
    });
  }

  private void checkCandidates(final JComboBox<String> inComboBox) {
    String name = (String) inComboBox.getSelectedItem();

    if (name != null) {
      candidate = new CandidateList(candidate);
      candidate.filter(model.getSavedCandidates(candidate.getPokemon(), name, candidate.getLimit()));
    }

    addCandidates();

    SwingUtilities.invokeLater(() -> candidateDetails.updateUI());
  }

  private JList<CandidateDetail> createList() {
    candidateDetails = new JList<>(new DefaultListModel<>());
    candidateDetails.setCellRenderer(new CandidateDetailRenderer());
    candidateDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    candidateDetails.addListSelectionListener(this::selected);
    addCandidates();
    SwingUtilities.invokeLater(() -> candidateDetails.updateUI());
    return candidateDetails;
  }

  private JPanel createTableHeader() {
    JPanel panel = new JPanel(new GridLayout(1, 9));
    panel.setBackground(Color.LIGHT_GRAY);
    panel.add(new JLabel(Translations.translate("label.level"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.short"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.code"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.IVattack"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.IVdefense"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.IVstamina"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("pct", Translations.translate("label.iv")), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.minCP"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("label.maxCP"), SwingConstants.CENTER));
    panel.add(new JLabel(Translations.translate("pct", Translations.translate("label.cp")), SwingConstants.CENTER));
    return panel;
  }

  private void initialize() {
    JPanel header = new JPanel(new GridBagLayout());
    JButton buttonCheck = new JButton(Translations.translate("button.check"));
    JButton buttonSave = new JButton(Translations.translate("default.button.save"));
    JButton buttonRemove = new JButton(Translations.translate("button.remove"));
    bestCandidate = new JLabel(" ", SwingConstants.RIGHT);
    comboBoxNames = new JComboBox<>();
    comboBoxNames.setEditable(true);

    buttonCheck.addActionListener(e -> checkCandidates(comboBoxNames));
    buttonSave.addActionListener(e -> saveCandidates(comboBoxNames));
    buttonRemove.addActionListener(e -> removeCandidate(comboBoxNames));

    header.add(buttonCheck, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
    header.add(comboBoxNames, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
    header.add(buttonSave, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
    header.add(buttonRemove, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
    header.add(bestCandidate, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    JScrollPane scroll = new JScrollPane(createList());
    scroll.getVerticalScrollBar().setUnitIncrement(20);
    scroll.getHorizontalScrollBar().setUnitIncrement(20);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    header.add(createTableHeader(), new GridBagConstraints(0, 1, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, ((Integer) UIManager.get("ScrollBar.width")).intValue()), 0, 0));

    add(header, BorderLayout.NORTH);
    add(scroll, BorderLayout.CENTER);
  }

  private void removeCandidate(final JComboBox<String> inComboBox) {
    String name = (String) inComboBox.getSelectedItem();

    if (name != null) {
      model.removeSavedCandidates(candidate.getPokemon(), name);
      inComboBox.removeItem(name);
    }
  }

  private void saveCandidates(final JComboBox<String> inComboBox) {
    String name = (String) inComboBox.getSelectedItem();

    if (name == null) {
      name = "";
    }

    name = name.trim();

    if (name.isEmpty()) {
      for (int i = 1; i < 1000; i++) {
        name = i + " " + candidate.getLimit();

        if (!names.contains(name)) {
          break;
        }
      }
    }

    model.saveCandidate(candidate, name);
    inComboBox.setSelectedItem(name);

    if (inComboBox.getSelectedIndex() == -1) {
      inComboBox.addItem(name);
      inComboBox.setSelectedItem(name);
    }
  }

  private void selected(final ListSelectionEvent inEvent) {
    if (!inEvent.getValueIsAdjusting()) {
      model.setSelectedDetails(candidateDetails.getSelectedValue());
    }
  }
}
