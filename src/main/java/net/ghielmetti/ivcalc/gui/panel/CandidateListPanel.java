package net.ghielmetti.ivcalc.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.gui.model.CandidateListModelIfc;
import net.ghielmetti.ivcalc.gui.renderer.CandidateListRenderer;
import net.ghielmetti.utilities.Translations;

/**
 * Displays a list of candidates that the user can select.
 *
 * @author Leopoldo Ghielmetti
 */
public class CandidateListPanel extends JPanel implements Observer {
  private JScrollPane           scrollCandidatesList;
  private JList<CandidateList>  candidatesList;
  private JCheckBox             halfLevels;
  private JTextField            total;
  private CandidateListModelIfc model;
  private List<CandidateList>   candidates = new ArrayList<>();

  /**
   * Constructor.
   *
   * @param inModel The model.
   */
  public CandidateListPanel(final CandidateListModelIfc inModel) {
    super(new GridBagLayout());

    model = inModel;
    model.addObserver(this);

    initialize();
  }

  /** Clears this view. */
  public void clear() {
    ((DefaultListModel<CandidateList>) candidatesList.getModel()).removeAllElements();
    model.setSelectedCandidate(null);

    scrollCandidatesList.getVerticalScrollBar().setValue(0);
    scrollCandidatesList.getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    if (CandidateListModelIfc.OBSERVE_HALF_LEVELS.equals(inArg)) {
      showCandidates();
    } else if (CandidateListModelIfc.OBSERVE_CANDIDATE_LIST.equals(inArg)) {
      candidates.clear();
      candidates.addAll(model.getCandidates());
      showCandidates();
    }
  }

  private void initialize() {
    halfLevels = new JCheckBox(Translations.translate("show.half.levels"));
    halfLevels.addActionListener(e -> model.setShowHalfLevels(halfLevels.isSelected()));
    halfLevels.setSelected(true);

    candidatesList = new JList<>();
    candidatesList.setCellRenderer(new CandidateListRenderer());
    candidatesList.setModel(new DefaultListModel<>());
    candidatesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    candidatesList.addListSelectionListener(inEvent -> {
      if (!inEvent.getValueIsAdjusting()) {
        model.setSelectedCandidate(candidatesList.getSelectedValue());
      }
    });

    scrollCandidatesList = new JScrollPane(candidatesList);
    scrollCandidatesList.getVerticalScrollBar().setUnitIncrement(20);
    scrollCandidatesList.getHorizontalScrollBar().setUnitIncrement(20);

    total = new JTextField("0");
    total.setEditable(false);
    total.setBorder(null);
    total.setFocusable(false);

    add(halfLevels, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(new JLabel(Translations.translate("label.total") + ":", SwingConstants.RIGHT), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(total, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(scrollCandidatesList, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private void showCandidates() {
    DefaultListModel<CandidateList> listModel = new DefaultListModel<>();
    CandidateList selected = model.getSelectedCandidate();
    int count = 0;

    for (CandidateList candidate : candidates) {
      candidate.setWithHalfLevels(halfLevels.isSelected());

      if (candidate.size() > 0) {
        listModel.addElement(candidate);
        count += candidate.size();
      }
    }

    candidatesList.setModel(listModel);
    total.setText(Integer.toString(count));
    candidatesList.setSelectedValue(selected, true);

    if (candidatesList.getSelectedValue() == null && !candidates.isEmpty()) {
      candidatesList.setSelectedIndex(0);
    }

    SwingUtilities.invokeLater(this::updateUI);
  }
}
