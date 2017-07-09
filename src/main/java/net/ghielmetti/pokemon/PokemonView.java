package net.ghielmetti.pokemon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.pokemon.team.Team;
import net.ghielmetti.utilities.Pair;
import net.ghielmetti.utilities.swing.JNumberField;

public class PokemonView extends JFrame {
  public interface ComputePokemonListener {
    void compute(List<String> inNames);
  }

  private final static Logger    VIEW = LoggerFactory.getLogger("Result");
  private ComputePokemonListener computePokemonListener;
  private JTextField             pokemonName;
  private JNumberField           cp;
  private JNumberField           hp;
  private JComboBox<Integer>     sd;
  private JComboBox<Limit>       code;
  private JList<String>          selected;
  private Team                   team;
  private JButton                compute;
  private JComboBox<String>      totalPanel;
  private JComboBox<String>      statsPanel;
  private JPanel                 appraisePanel;
  private JCheckBox              bestQualityHP;
  private JCheckBox              bestQualityAttack;
  private JCheckBox              bestQualityDefense;
  private JPanel                 panelCode;
  private JPanel                 panelSD;
  private JScrollPane            scrollList;
  private JScrollPane            scrollAppender;
  private JPanel                 candidatesList;
  private JMenuBar               menuBar;
  private String                 version;

  public PokemonView(final ComputePokemonListener inComputePokemonListener, final Team inTeam) {
    computePokemonListener = inComputePokemonListener;
    team = inTeam;
    version = new VersionReader().getVersion();
    initialize();
  }

  public void clear() {
    candidatesList.removeAll();
    candidatesList.add(new JPanel(), new GridBagConstraints(0, 10000, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    scrollAppender.updateUI();
  }

  public String getApproximatedName() {
    return pokemonName.getText();
  }

  public Limit getCode() {
    return (Limit) code.getSelectedItem();
  }

  public Integer getCP() {
    return cp.getNumber().intValue();
  }

  public Integer getHP() {
    return hp.getNumber().intValue();
  }

  public Logger getOutput() {
    return VIEW;
  }

  public Integer getSD() {
    return (Integer) sd.getSelectedItem();
  }

  public void outputCandidate(final Item inItem, final Integer inCP, final List<Pair<Integer, IVLevel>> inCandidates) {
    candidatesList.add(new CandidatePanel(inItem, inCP, inCandidates), new GridBagConstraints(0, candidatesList.getComponentCount() - 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    scrollAppender.updateUI();
  }

  private void defineAppraisePanel() {
    defineTotalPanel();
    defineStatsPanel();

    appraisePanel = new JPanel(new GridBagLayout());
    bestQualityHP = new JCheckBox("HP");
    bestQualityHP.addActionListener(e -> selectAppreciation());
    bestQualityAttack = new JCheckBox("Attack");
    bestQualityAttack.addActionListener(e -> selectAppreciation());
    bestQualityDefense = new JCheckBox("Defense");
    bestQualityDefense.addActionListener(e -> selectAppreciation());
    appraisePanel.add(totalPanel, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    appraisePanel.add(bestQualityHP, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    appraisePanel.add(bestQualityAttack, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(2, 2, 2, 2), 0, 0));
    appraisePanel.add(bestQualityDefense, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    appraisePanel.add(statsPanel, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
  }

  private void defineComputeButton() {
    compute = new JButton("Calculate");
    compute.addActionListener(e -> computePokemonListener.compute(selected.getSelectedValuesList()));
    compute.setDefaultCapable(true);
    compute.setMnemonic(KeyEvent.VK_C);
  }

  private void defineFields() {
    pokemonName = new JTextField();
    selected = new JList<>(Pokedex.getInstance().getAllNames().toArray(new String[0]));
    cp = new JNumberField(5);
    hp = new JNumberField(5);
    sd = new JComboBox<>(Multiplier.getInstance().getStardustList().toArray(new Integer[0]));
    code = new JComboBox<>(IVMap.getLimits().toArray(new Limit[0]));
    code.addActionListener(this::codeSelected);
    candidatesList = new JPanel(new GridBagLayout());
    scrollAppender = new JScrollPane(candidatesList);
    scrollList = new JScrollPane(selected);
    selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    pokemonName.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(final KeyEvent e) {
        SwingUtilities.invokeLater(PokemonView.this::select);
      }
    });
    selected.setSelectionInterval(0, selected.getModel().getSize() - 1);
  }

  private void defineMenu() {
    JMenuItem exit = new JMenuItem("Exit");
    exit.setMnemonic(KeyEvent.VK_X);
    exit.addActionListener(e -> dispose());

    JMenuItem about = new JMenuItem("About");
    about.setMnemonic(KeyEvent.VK_A);
    about.addActionListener(e -> showAbout());

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.add(exit);

    JMenu helpMenu = new JMenu("Help");
    helpMenu.setMnemonic(KeyEvent.VK_H);
    helpMenu.add(about);

    menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(helpMenu);
  }

  private void definePanelCode() {
    panelCode = new JPanel(new BorderLayout());
    panelCode.add(code, BorderLayout.CENTER);
  }

  private void definePanelSD() {
    panelSD = new JPanel(new BorderLayout());
    panelSD.add(sd, BorderLayout.CENTER);
  }

  private void defineStatsPanel() {
    statsPanel = new JComboBox<>();
    for (int i = 1; i <= 4; i++) {
      statsPanel.addItem(team.getStatsLabel(i));
    }
    statsPanel.addActionListener(e -> selectAppreciation());
  }

  private void defineTotalPanel() {
    totalPanel = new JComboBox<>();
    for (int i = 1; i <= 4; i++) {
      totalPanel.addItem(team.getTotalLabel(i));
    }
    totalPanel.addActionListener(e -> selectAppreciation());
  }

  private void initialize() {
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Pokémon GO IV calculator version " + version + ", team: " + team.getTeamName());

    defineMenu();
    defineFields();
    definePanelSD();
    definePanelCode();
    defineComputeButton();
    defineAppraisePanel();

    code.setSelectedIndex(0);

    getContentPane().setLayout(new GridBagLayout());
    getContentPane().add(pokemonName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(cp, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(hp, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(panelSD, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(panelCode, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(compute, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(scrollList, new GridBagConstraints(0, 1, 1, 3, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(appraisePanel, new GridBagConstraints(1, 1, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(scrollAppender, new GridBagConstraints(1, 2, 5, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    getRootPane().setDefaultButton(compute);
    setJMenuBar(menuBar);
    setPreferredSize(new Dimension(800, 600));
    setResizable(true);
    resetSizes();
    setLocationRelativeTo(null);
  }

  private void resetSizes() {
    pack();
    pokemonName.setPreferredSize(new Dimension(130, (int) compute.getSize().getHeight()));
    totalPanel.setPreferredSize(new Dimension(200, (int) totalPanel.getSize().getHeight()));
    statsPanel.setPreferredSize(new Dimension(200, (int) statsPanel.getSize().getHeight()));
    pack();
  }

  private void selectAppreciation() {
    Limit limit = new Limit(totalPanel.getSelectedIndex() + 1, statsPanel.getSelectedIndex() + 1, bestQualityHP.isSelected(), bestQualityAttack.isSelected(), bestQualityDefense.isSelected());
    code.setSelectedItem(limit);
    codeSelected(null);
  }

  private void selectButton(final int inButtonNumber, final Enumeration<AbstractButton> inEnumeration) {
    AbstractButton button = null;
    for (int i = inButtonNumber; i > 0; i--) {
      button = inEnumeration.nextElement();
    }
    if ((button != null) && !button.isSelected()) {
      button.setSelected(true);
    }
  }

  private void showAbout() {
    JOptionPane.showMessageDialog(this, "Pokémon GO IV calculator\nVersion: " + version + "\nCopyright 2017 by Leopoldo Ghielmetti\nDistributed under GPLv3!", "Pokémon GO IV calculator", JOptionPane.PLAIN_MESSAGE);
  }

  void codeSelected(final ActionEvent inEvent) {
    Limit limit = (Limit) code.getSelectedItem();
    if (bestQualityHP.isSelected() != limit.isHP()) {
      bestQualityHP.setSelected(limit.isHP());
    }
    if (bestQualityAttack.isSelected() != limit.isAttack()) {
      bestQualityAttack.setSelected(limit.isAttack());
    }
    if (bestQualityDefense.isSelected() != limit.isDefense()) {
      bestQualityDefense.setSelected(limit.isDefense());
    }
    totalPanel.setSelectedIndex(limit.getLevel() - 1);
    statsPanel.setSelectedIndex(limit.getStrength() - 1);
  }

  void select() {
    String search = pokemonName.getText().trim();
    int show = -1;
    selected.clearSelection();
    for (String name : Pokedex.getInstance().getNamesFromPartialName(search)) {
      for (int index = selected.getModel().getSize() - 1; index >= 0; index--) {
        if (name.equalsIgnoreCase(selected.getModel().getElementAt(index))) {
          selected.addSelectionInterval(index, index);
          if (show == -1) {
            show = index;
          }
        }
      }
    }
    selected.ensureIndexIsVisible(show);
  }
}
