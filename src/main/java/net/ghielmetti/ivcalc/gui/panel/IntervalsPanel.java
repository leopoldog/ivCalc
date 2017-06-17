package net.ghielmetti.ivcalc.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.gui.model.IntervalsModelIfc;
import net.ghielmetti.ivcalc.gui.model.PokemonModel;
import net.ghielmetti.utilities.Translations;
import net.ghielmetti.utilities.swing.Interval;
import net.ghielmetti.utilities.swing.JMultiValueRuler;

/**
 * A {@link JPanel} that contains the {@link JMultiValueRuler} for the Pokémon characteristics.
 *
 * @author Leopoldo Ghielmetti
 */
public class IntervalsPanel extends JPanel implements Observer {
  private static final IVLevel      IV_MIN      = new IVLevel(0, 0, 0);
  private static final IVLevel      IV_MAX      = new IVLevel(15, 15, 15);
  private static final Logger       LOGGER      = LoggerFactory.getLogger(IntervalsPanel.class);
  private JMultiValueRuler<Integer> rulerA;
  private JMultiValueRuler<Integer> rulerD;
  private JMultiValueRuler<Integer> rulerS;
  private JMultiValueRuler<Integer> rulerCP;
  private Interval<Integer>         intervalA1  = new Interval<>();
  private Interval<Integer>         intervalA2  = new Interval<>();
  private Interval<Integer>         intervalA3  = new Interval<>();
  private Interval<Integer>         intervalD1  = new Interval<>();
  private Interval<Integer>         intervalD2  = new Interval<>();
  private Interval<Integer>         intervalD3  = new Interval<>();
  private Interval<Integer>         intervalS1  = new Interval<>();
  private Interval<Integer>         intervalS2  = new Interval<>();
  private Interval<Integer>         intervalS3  = new Interval<>();
  private Interval<Integer>         intervalCP1 = new Interval<>();
  private Interval<Integer>         intervalCP2 = new Interval<>();
  private Interval<Integer>         intervalCP3 = new Interval<>();
  private IntervalsModelIfc         model;
  private ScheduledExecutorService  executor;
  private Pokemon                   selectedPokemon;
  private Pokemon                   displayedPokemon;
  private CandidateDetail           selectedDetail;
  private CandidateDetail           displayedDetail;

  /**
   * Creates a new JPanel using the specified {@link PokemonModel}.
   *
   * @param inModel The model.
   */
  public IntervalsPanel(final IntervalsModelIfc inModel) {
    super(new GridBagLayout());

    model = inModel;
    model.addObserver(this);

    initialize();

    addAncestorListener(new AncestorListener() {
      @Override
      public void ancestorAdded(final AncestorEvent event) {
        start();
      }

      @Override
      public void ancestorMoved(final AncestorEvent event) {
        // nothing to do
      }

      @Override
      public void ancestorRemoved(final AncestorEvent event) {
        stop();
      }
    });
  }

  /** Clears the rulers. */
  public synchronized void clear() {
    selectedPokemon = null;
    displayedPokemon = null;
    selectedDetail = null;
    displayedDetail = null;
  }

  @Override
  public synchronized void update(final Observable inObservable, final Object inArg) {
    if (IntervalsModelIfc.OBSERVE_SELECTED_CANDIDATE.equals(inArg)) {
      selectedPokemon = model.getSelectedCandidate() == null ? null : model.getSelectedCandidate().getPokemon();
      selectedDetail = null;
    } else if (IntervalsModelIfc.OBSERVE_SELECTED_DETAIL.equals(inArg)) {
      selectedPokemon = model.getSelectedCandidate() == null ? null : model.getSelectedCandidate().getPokemon();
      selectedDetail = model.getSelectedDetail();
    }
  }

  private JMultiValueRuler<Integer> defineRuler(final Integer inMin, final Integer inMax, final Interval<Integer> inInterval1, final Interval<Integer> inInterval2, final Interval<Integer> inInterval3) {
    inInterval1.setBackground(Color.LIGHT_GRAY);
    inInterval1.setForeground(Color.GRAY);
    inInterval2.setBackground(Color.YELLOW);
    inInterval2.setForeground(Color.GRAY);
    inInterval3.setBackground(Color.WHITE);
    inInterval3.setForeground(Color.GRAY);
    inInterval3.setForegroundValueColor(Color.BLACK);
    inInterval3.setBackgroundValueColor(Color.GREEN);

    JMultiValueRuler<Integer> ruler = new JMultiValueRuler<>(inMin, inMax);
    ruler.setPreferredSize(new Dimension(50, 50));
    ruler.setMinimumSize(new Dimension(50, 50));
    ruler.setOpaque(true);
    ruler.setBorder(new BevelBorder(BevelBorder.LOWERED));
    ruler.setBackground(Color.DARK_GRAY);
    ruler.setForeground(Color.WHITE);
    ruler.addInterval(Integer.valueOf(1), inInterval1);
    ruler.addInterval(Integer.valueOf(2), inInterval2);
    ruler.addInterval(Integer.valueOf(3), inInterval3);
    return ruler;
  }

  private void initialize() {
    int cpMax = 0;
    int aMax = 0;
    int dMax = 0;
    int sMax = 0;
    int aMin = 1000;
    int dMin = 1000;
    int sMin = 1000;
    for (Pokemon pokemon : model.getAllPokemons()) {
      cpMax = Math.max(cpMax, pokemon.getCPForLevel(80, IV_MAX));
      ImmutableTriple<Integer, Integer, Integer> valuesMax = pokemon.getValues(80, IV_MAX);
      aMax = Math.max(aMax, valuesMax.getLeft().intValue());
      dMax = Math.max(dMax, valuesMax.getMiddle().intValue());
      sMax = Math.max(sMax, valuesMax.getRight().intValue());
      ImmutableTriple<Integer, Integer, Integer> valuesMin = pokemon.getValues(2, IV_MIN);
      aMin = Math.min(aMin, valuesMin.getLeft().intValue());
      dMin = Math.min(dMin, valuesMin.getMiddle().intValue());
      sMin = Math.min(sMin, valuesMin.getRight().intValue());
    }

    initializeRulerA(aMin, aMax);
    initializeRulerD(dMin, dMax);
    initializeRulerS(sMin, sMax);
    initializeRulerCP(10, cpMax);
  }

  private void initializeRulerA(final int inMin, final int inMax) {
    rulerA = defineRuler(Integer.valueOf(inMin), Integer.valueOf(inMax), intervalA1, intervalA2, intervalA3);
    JLabel labelA = new JLabel(Translations.translate("rulerA.title"));
    labelA.setHorizontalAlignment(SwingConstants.CENTER);
    labelA.setBackground(Color.DARK_GRAY);
    labelA.setForeground(Color.WHITE);
    labelA.setOpaque(true);
    add(labelA, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(rulerA, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private void initializeRulerCP(final int inMin, final int inMax) {
    rulerCP = defineRuler(Integer.valueOf(inMin), Integer.valueOf(inMax), intervalCP1, intervalCP2, intervalCP3);
    JLabel labelCP = new JLabel(Translations.translate("rulerCP.title"));
    labelCP.setHorizontalAlignment(SwingConstants.CENTER);
    labelCP.setBackground(Color.DARK_GRAY);
    labelCP.setForeground(Color.WHITE);
    labelCP.setOpaque(true);
    add(labelCP, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(rulerCP, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private void initializeRulerD(final int inMin, final int inMax) {
    rulerD = defineRuler(Integer.valueOf(inMin), Integer.valueOf(inMax), intervalD1, intervalD2, intervalD3);
    JLabel labelD = new JLabel(Translations.translate("rulerD.title"));
    labelD.setHorizontalAlignment(SwingConstants.CENTER);
    labelD.setBackground(Color.DARK_GRAY);
    labelD.setForeground(Color.WHITE);
    labelD.setOpaque(true);
    add(labelD, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(rulerD, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private void initializeRulerS(final int inMin, final int inMax) {
    rulerS = defineRuler(Integer.valueOf(inMin), Integer.valueOf(inMax), intervalS1, intervalS2, intervalS3);
    JLabel labelS = new JLabel(Translations.translate("rulerS.title"));
    labelS.setHorizontalAlignment(SwingConstants.CENTER);
    labelS.setBackground(Color.DARK_GRAY);
    labelS.setForeground(Color.WHITE);
    labelS.setOpaque(true);
    add(labelS, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(rulerS, new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private void setIntervals(final Interval<Integer> iA, final Interval<Integer> iD, final Interval<Integer> iS, final ImmutableTriple<Integer, Integer, Integer> inMin, final ImmutableTriple<Integer, Integer, Integer> inMax) {
    iA.setMinimum(inMin.getLeft());
    iA.setMaximum(inMax.getLeft());
    iA.setVisible(true);
    iD.setMinimum(inMin.getMiddle());
    iD.setMaximum(inMax.getMiddle());
    iD.setVisible(true);
    iS.setMinimum(inMin.getRight());
    iS.setMaximum(inMax.getRight());
    iS.setVisible(true);
  }

  private void updateRulers() {
    try {
      Pokemon pokemon;
      Integer level;
      IVLevel ivLevel;

      synchronized (this) {
        if (Objects.equals(selectedDetail, displayedDetail) && Objects.equals(selectedPokemon, displayedPokemon)) {
          return;
        }

        displayedDetail = selectedDetail;
        displayedPokemon = selectedPokemon;

        if (displayedPokemon == null) {
          intervalA1.setVisible(false);
          intervalA2.setVisible(false);
          intervalA3.setVisible(false);
          intervalD1.setVisible(false);
          intervalD2.setVisible(false);
          intervalD3.setVisible(false);
          intervalS1.setVisible(false);
          intervalS2.setVisible(false);
          intervalS3.setVisible(false);
          intervalCP1.setVisible(false);
          intervalCP2.setVisible(false);
          intervalCP3.setVisible(false);
          getRootPane().updateUI();
          return;
        }

        pokemon = displayedPokemon;
        level = displayedDetail == null ? null : displayedDetail.getLevel();
        ivLevel = displayedDetail == null ? null : displayedDetail.getIVLevel();
      }

      ImmutableTriple<Integer, Integer, Integer> valuesMin = pokemon.getValues(2, IV_MIN);
      ImmutableTriple<Integer, Integer, Integer> valuesMax = pokemon.getValues(80, IV_MAX);
      setIntervals(intervalA1, intervalD1, intervalS1, valuesMin, valuesMax);

      if (ivLevel != null && level != null) {
        ImmutableTriple<Integer, Integer, Integer> valuesMinLevel = pokemon.getValues(2, ivLevel);
        ImmutableTriple<Integer, Integer, Integer> valuesMaxLevel = pokemon.getValues(80, ivLevel);
        setIntervals(intervalA2, intervalD2, intervalS2, valuesMinLevel, valuesMaxLevel);

        ImmutableTriple<Integer, Integer, Integer> valuesLevelMin = pokemon.getValues(level.intValue(), IV_MIN);
        ImmutableTriple<Integer, Integer, Integer> valuesLevelMax = pokemon.getValues(level.intValue(), IV_MAX);
        setIntervals(intervalA3, intervalD3, intervalS3, valuesLevelMin, valuesLevelMax);

        int cpLevel2 = pokemon.getCPForLevel(2, ivLevel);
        int cpLevel40 = pokemon.getCPForLevel(80, ivLevel);
        ImmutablePair<Integer, Integer> minMaxLevel = pokemon.getMinMaxCP(level.intValue());
        ImmutablePair<Integer, Integer> minMax1 = pokemon.getMinMaxCP(2);
        ImmutablePair<Integer, Integer> minMax40 = pokemon.getMinMaxCP(80);
        ImmutableTriple<Integer, Integer, Integer> min = ImmutableTriple.of(minMax1.getLeft(), Integer.valueOf(cpLevel2), minMaxLevel.getLeft());
        ImmutableTriple<Integer, Integer, Integer> max = ImmutableTriple.of(minMax40.getRight(), Integer.valueOf(cpLevel40), minMaxLevel.getRight());
        setIntervals(intervalCP1, intervalCP2, intervalCP3, min, max);

        ImmutableTriple<Integer, Integer, Integer> valuesLevel = pokemon.getValues(level.intValue(), ivLevel);
        intervalA3.setValue(valuesLevel.getLeft());
        intervalD3.setValue(valuesLevel.getMiddle());
        intervalS3.setValue(valuesLevel.getRight());

        int cpLevelX = pokemon.getCPForLevel(level.intValue(), ivLevel);
        intervalCP3.setValue(Integer.valueOf(cpLevelX));
      } else {
        intervalA2.setVisible(false);
        intervalA3.setVisible(false);
        intervalD2.setVisible(false);
        intervalD3.setVisible(false);
        intervalS2.setVisible(false);
        intervalS3.setVisible(false);
        intervalCP2.setVisible(false);
        intervalCP3.setVisible(false);

        ImmutablePair<Integer, Integer> minMax1 = pokemon.getMinMaxCP(2);
        ImmutablePair<Integer, Integer> minMax40 = pokemon.getMinMaxCP(80);
        intervalCP1.setMinimum(minMax1.getLeft());
        intervalCP1.setMaximum(minMax40.getRight());
        intervalCP1.setVisible(true);
      }

      getRootPane().updateUI();
    } catch (Exception e) {
      LOGGER.warn("Error while updating the intervals", e);
    }
  }

  /**
   * Start the interval updater.<br>
   * For performance reasons, the {@link Interval}s are updates only each 200ms.
   */
  synchronized void start() {
    if (executor == null) {
      clear();
      executor = Executors.newSingleThreadScheduledExecutor();
      executor.scheduleAtFixedRate(this::updateRulers, 200, 200, TimeUnit.MILLISECONDS);
    }
  }

  /** Stops the interval updater. */
  synchronized void stop() {
    if (executor != null) {
      executor.shutdown();
      executor = null;
      clear();
      updateRulers();
    }
  }
}
