package net.ghielmetti.ivcalc.gui.model;

import java.util.List;
import java.util.Observer;

import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.gui.panel.CandidateDetailsPanel;

/**
 * The model for a {@link CandidateDetailsPanel}.
 *
 * @author Leopoldo Ghielmetti
 */
public interface CandidateDetailsModelIfc extends GoodnessModelIfc {
  /** Identifier for observation of selected candidate change. */
  String OBSERVE_SELECTED_CANDIDATE = "SelectedCandidate";

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
  Integer getCP();

  /**
   * Retrieves a saved {@link CandidateList} from the database.
   *
   * @param inPokemon The Pokémon who the details belong.
   * @param inName The name of the saved details.
   * @param inLimit The limits.
   * @return The retrieved {@link CandidateList}.
   */
  CandidateList getSavedCandidates(Pokemon inPokemon, String inName, Limit inLimit);

  /**
   * Searches in the database the list of names for the saved candidates.
   *
   * @param inPokemon The Pokémon who the details belongs.
   * @param inLimit The limits.
   * @return The list of saved names.
   */
  List<String> getSavedCandidatesNames(Pokemon inPokemon, Limit inLimit);

  /**
   * Accessor.
   *
   * @return The selected {@link CandidateList}.
   */
  CandidateList getSelectedCandidate();

  /**
   * Remove the saved candidate from the database.
   *
   * @param inPokemon The Pokémon to remove.
   * @param inName The name of the saved details.
   */
  void removeSavedCandidates(Pokemon inPokemon, String inName);

  /**
   * Save the {@link CandidateList} with the given name.
   *
   * @param inCandidates The candidates to save.
   * @param inName The name to use for save.
   */
  void saveCandidate(CandidateList inCandidates, String inName);

  /**
   * Sets the details as choosen by the user.
   *
   * @param inSelectedDetail The new details
   */
  void setSelectedDetails(CandidateDetail inSelectedDetail);
}
