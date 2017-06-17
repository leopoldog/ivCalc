package net.ghielmetti.ivcalc.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.gui.menu.PokemonMenu;
import net.ghielmetti.ivcalc.gui.model.PokemonModelIfc;
import net.ghielmetti.ivcalc.gui.panel.AppraisePanel;
import net.ghielmetti.ivcalc.gui.panel.CandidateDetailsPanel;
import net.ghielmetti.ivcalc.gui.panel.CandidateListPanel;
import net.ghielmetti.ivcalc.gui.panel.IntervalsPanel;
import net.ghielmetti.ivcalc.gui.panel.PokemonChooserPanel;
import net.ghielmetti.ivcalc.gui.panel.ValuesPanel;
import net.ghielmetti.utilities.Translations;

/**
 * The main view of the Pokémon application.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonView extends JFrame implements Observer {
  private static final Logger   VIEW = LoggerFactory.getLogger("Result");
  private JButton               search;
  private AppraisePanel         appraisePanel;
  private CandidateListPanel    candidateListPanel;
  private IntervalsPanel        intervalsPanel;
  private JPanel                candidatesDetailsPanel;
  private JSplitPane            centerSplit;
  private ValuesPanel           valuesPanel;
  private PokemonChooserPanel   pokemonsPanel;
  private final ActionListener  searchPokemonListener;
  private final ActionListener  changeTeamListener;
  private final ActionListener  changeLanguageListener;
  private final ActionListener  showAboutListener;
  private final ActionListener  showOptionsListener;
  private final PokemonModelIfc model;

  /**
   * Constructor.
   *
   * @param inModel The model.
   * @param inSearchPokemonListener Called when the search button is pressed.
   * @param inChangeTeamListener Called when the change team menu is selected.
   * @param inShowAboutListener Called when the about menu is selected.
   * @param inShowOptionsListener Called when the options menu is selected.
   * @param inChangeLanguageListener Called when the language menu is selected.
   */
  public PokemonView(final PokemonModelIfc inModel, final ActionListener inSearchPokemonListener, final ActionListener inChangeTeamListener, final ActionListener inShowAboutListener, final ActionListener inShowOptionsListener,
      final ActionListener inChangeLanguageListener) {
    super();

    searchPokemonListener = inSearchPokemonListener;
    changeTeamListener = inChangeTeamListener;
    changeLanguageListener = inChangeLanguageListener;
    showAboutListener = inShowAboutListener;
    showOptionsListener = inShowOptionsListener;

    model = inModel;
    model.addObserver(this);

    initialize();
  }

  /** Clears the view. */
  public void clear() {
    candidateListPanel.clear();
    intervalsPanel.clear();

    reset();
  }

  @Override
  public void dispose() {
    model.setViewBounds(getBounds());
    model.setViewExtendedState(getExtendedState());
    super.dispose();
  }

  /**
   * Returns the output Logger for displaying the Pokémon results.
   *
   * @return The {@link Logger}
   */
  public Logger getOutput() {
    return VIEW;
  }

  /**
   * Lock/Unlock the interface buttons.
   *
   * @param inLock <code>true</code> to lock, <code>false</code> to unlock.
   */
  public void lockButtons(final boolean inLock) {
    search.setEnabled(!inLock);
  }

  /** Reset the view for the next input. */
  public void reset() {
    if (!pokemonsPanel.requestFocusInWindow()) {
      valuesPanel.requestFocusInWindow();
    }

    getRootPane().updateUI();
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    // nothing to do
  }

  private void initialize() {
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    setJMenuBar(new PokemonMenu(changeTeamListener, changeLanguageListener, showOptionsListener, showAboutListener, e -> dispose()));

    search = new JButton(Translations.translate("button.search"));
    search.addActionListener(searchPokemonListener);
    search.setDefaultCapable(true);
    search.setMnemonic(KeyEvent.VK_C);

    valuesPanel = new ValuesPanel(model);
    pokemonsPanel = new PokemonChooserPanel(model);
    appraisePanel = new AppraisePanel(model);
    intervalsPanel = new IntervalsPanel(model);
    candidateListPanel = new CandidateListPanel(model);
    candidatesDetailsPanel = new CandidateDetailsPanel(model);

    centerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    centerSplit.add(candidateListPanel);
    centerSplit.add(candidatesDetailsPanel);

    getContentPane().setLayout(new GridBagLayout());
    getContentPane().add(pokemonsPanel, new GridBagConstraints(0, 0, 1, 4, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(valuesPanel, new GridBagConstraints(1, 0, 4, 2, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(new JLabel(" "), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(search, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(appraisePanel, new GridBagConstraints(1, 2, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(centerSplit, new GridBagConstraints(1, 3, 4, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    getContentPane().add(intervalsPanel, new GridBagConstraints(5, 2, 1, 2, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    getRootPane().setDefaultButton(search);
    setResizable(true);
    resetSizes();
  }

  private void resetSizes() {
    pack();

    // Dirty but because it is in a different panel, is the only way I've found to do it.
    pokemonsPanel.setNameFieldHeight((int) search.getSize().getHeight());

    Rectangle bounds = model.getViewBounds();
    setPreferredSize(bounds.getSize());

    setBounds(bounds);
    if ((int) bounds.getX() == Integer.MAX_VALUE || (int) bounds.getY() == Integer.MAX_VALUE) {
      setLocationRelativeTo(null);
    } else {
      setBounds(bounds);
    }

    setExtendedState(model.getViewExtendedState());
    SwingUtilities.invokeLater(() -> centerSplit.setDividerLocation(0.5));
  }
}
