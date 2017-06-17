package net.ghielmetti.ivcalc.gui.model;

import java.util.Observer;

import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.gui.panel.ValuesPanel;

/**
 * The model for {@link ValuesPanel}.
 *
 * @author Leopoldo Ghielmetti
 */
public interface ValuesModelIfc {
  /** Identifier for observation of Limit changes. */
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
   * @return The value for CP.
   */
  Number getCP();

  /**
   * Accessor.
   *
   * @return The value for HP.
   */
  Number getHP();

  /**
   * Accessor.
   *
   * @return The selected Limit.
   */
  Limit getLimit();

  /**
   * Sets the CP value.
   *
   * @param inCP The new CP value.
   */
  void setCP(Integer inCP);

  /**
   * Sets the HP value.
   *
   * @param inHP The new HP value.
   */
  void setHP(Integer inHP);

  /**
   * Sets the limit value.
   *
   * @param inLimit The new limits.
   */
  void setLimit(Limit inLimit);

  /**
   * Sets the limit value.
   *
   * @param inCode The new limits (in {@link String} form).
   */
  void setLimit(String inCode);

  /**
   * Sets the SD value.
   *
   * @param inSD The new SD value.
   */
  void setSD(Integer inSD);
}
