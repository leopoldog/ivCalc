package net.ghielmetti.ivcalc.gui.model;

import java.util.List;

import javax.swing.table.TableModel;

import net.ghielmetti.ivcalc.data.GoodnessChecker;

/**
 * A {@link TableModel} that manages the {@link GoodnessChecker} list.
 *
 * @author Leopoldo Ghielmetti
 */
public interface GoodnessCheckerModelIfc extends TableModel, GoodnessModelIfc {
  /**
   * Creates a new {@link GoodnessChecker} with the specified name.
   *
   * @param inName The name of the new {@link GoodnessChecker}.
   */
  void createChecker(String inName);

  /**
   * Deletes the specified checker from the list.
   *
   * @param inRowIndex The index of the checker to remove.
   */
  void deleteChecker(int inRowIndex);

  /**
   * Returns all the Pokémon names that haven't an associated {@link GoodnessChecker}.
   *
   * @return The list of {@link String}
   */
  List<String> getAllAvailableNames();

  /** Save the {@link GoodnessChecker} to configuration. */
  void save();
}
