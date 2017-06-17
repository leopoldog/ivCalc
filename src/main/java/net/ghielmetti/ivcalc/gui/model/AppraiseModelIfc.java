package net.ghielmetti.ivcalc.gui.model;

import java.util.Observer;

import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.gui.panel.AppraisePanel;

/**
 * Model for the {@link AppraisePanel}.
 *
 * @author Leopoldo Ghielmetti
 */
public interface AppraiseModelIfc {
  /** Identifier for observation of Limit change. */
  String OBSERVE_LIMIT = "Limit";

  /**
   * Adds an observer to the set of observers for this object, provided that it is not the same as some observer already
   * in the set. The order in which notifications will be delivered to multiple observers is not specified. See the
   * class comment.
   *
   * @param inObserver an observer to be added.
   * @throws NullPointerException if the parameter inObserver is null.
   */
  void addObserver(Observer inObserver);

  /**
   * Accessor.
   *
   * @return The selected Limit.
   */
  Limit getLimit();

  /**
   * Returns the label for the specified Pokémon stat value.
   *
   * @param intValue The stat value (0, 1, 2 or 3)
   * @return The corresponding label.
   */
  String getStatsLabel(int intValue);

  /**
   * Returns the label for the specified Pokémon total value.
   *
   * @param intValue The total value (0, 1, 2 or 3)
   * @return The corresponding label.
   */
  String getTotalLabel(int intValue);

  /**
   * Sets the attack as best quality in the limit information.
   *
   * @param inBestQualityAttack <code>true</code> if the attack is the best quality.
   */
  void setBestQualityAttack(boolean inBestQualityAttack);

  /**
   * Sets the defense as best quality in the limit information.
   *
   * @param inBestQualityDefense <code>true</code> if the defense is the best quality.
   */
  void setBestQualityDefense(boolean inBestQualityDefense);

  /**
   * Sets the HP as best quality in the limit information.
   *
   * @param inBestQualityHP <code>true</code> if the HP is the best quality.
   */
  void setBestQualityHP(boolean inBestQualityHP);

  /**
   * Sets the new stats value in the limit information.
   *
   * @param inStats The new stats value.
   */
  void setStats(int inStats);

  /**
   * Sets the new total value in the limit information.
   *
   * @param inTotal The new total value.
   */
  void setTotal(int inTotal);
}
