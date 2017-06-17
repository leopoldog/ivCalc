package net.ghielmetti.ivcalc.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Function;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.gui.model.AppraiseModelIfc;
import net.ghielmetti.utilities.Translations;
import net.ghielmetti.utilities.swing.Scroller;

/**
 * A {@link JPanel} that enables the user to choose the Pokémon characteristics following the sentences of the Leader.
 *
 * @author Leopoldo Ghielmetti
 */
public class AppraisePanel extends JPanel implements Observer {
  private static final Logger LOGGER = LoggerFactory.getLogger(AppraisePanel.class);
  private JComboBox<String>   total;
  private JComboBox<String>   stats;
  private JCheckBox           bestQualityHP;
  private JCheckBox           bestQualityAttack;
  private JCheckBox           bestQualityDefense;
  private AppraiseModelIfc    model;

  /**
   * Creates a new panel.
   *
   * @param inModel The model.
   */
  public AppraisePanel(final AppraiseModelIfc inModel) {
    super(new GridBagLayout());

    model = inModel;
    model.addObserver(this);

    initialize();
  }

  /**
   * Returns the document model.
   *
   * @return The model.
   */
  public AppraiseModelIfc getModel() {
    return model;
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    if (AppraiseModelIfc.OBSERVE_LIMIT.equals(inArg)) {
      codeSelected(model.getLimit());
    }
  }

  private synchronized void codeSelected(final Limit inLimit) {
    LOGGER.debug("Update Limit: {}", inLimit);

    if (bestQualityHP.isSelected() != inLimit.isHP()) {
      bestQualityHP.setSelected(inLimit.isHP());
    }

    if (bestQualityAttack.isSelected() != inLimit.isAttack()) {
      bestQualityAttack.setSelected(inLimit.isAttack());
    }

    if (bestQualityDefense.isSelected() != inLimit.isDefense()) {
      bestQualityDefense.setSelected(inLimit.isDefense());
    }

    if (total.getSelectedIndex() != 3 - inLimit.getLevel()) {
      total.setSelectedIndex(3 - inLimit.getLevel());
    }

    if (stats.getSelectedIndex() != 3 - inLimit.getStrength()) {
      stats.setSelectedIndex(3 - inLimit.getStrength());
    }
  }

  private JComboBox<String> defineLabelsPanel(final Function<Integer, String> inGetLabel, final Function<Integer, Void> inSetValue) {
    JComboBox<String> comboBox = new JComboBox<>();

    for (int i = 0; i < 4; i++) {
      comboBox.addItem(inGetLabel.apply(Integer.valueOf(4 - i)));
    }

    comboBox.addActionListener(e -> {
      @SuppressWarnings("unchecked")
      JComboBox<String> source = (JComboBox<String>) e.getSource();
      inSetValue.apply(Integer.valueOf(3 - source.getSelectedIndex()));
    });

    comboBox.addMouseWheelListener(new Scroller());

    return comboBox;
  }

  private void initialize() {
    total = defineLabelsPanel(i -> model.getTotalLabel(i.intValue()), i -> {
      model.setTotal(i.intValue());
      return null;
    });

    stats = defineLabelsPanel(i -> model.getStatsLabel(i.intValue()), i -> {
      model.setStats(i.intValue());
      return null;
    });

    bestQualityHP = new JCheckBox(Translations.translate("label.hp"));
    bestQualityHP.addActionListener(e -> model.setBestQualityHP(bestQualityHP.isSelected()));

    bestQualityAttack = new JCheckBox(Translations.translate("label.attack"));
    bestQualityAttack.addActionListener(e -> model.setBestQualityAttack(bestQualityAttack.isSelected()));

    bestQualityDefense = new JCheckBox(Translations.translate("label.defense"));
    bestQualityDefense.addActionListener(e -> model.setBestQualityDefense(bestQualityDefense.isSelected()));

    add(total, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    add(bestQualityHP, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    add(bestQualityAttack, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(2, 2, 2, 2), 0, 0));
    add(bestQualityDefense, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    add(stats, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

    SwingUtilities.invokeLater(() -> codeSelected(model.getLimit()));
  }
}
