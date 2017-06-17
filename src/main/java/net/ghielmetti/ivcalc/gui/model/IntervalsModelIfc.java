package net.ghielmetti.ivcalc.gui.model;

import java.util.Collection;
import java.util.Observer;

import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.gui.panel.IntervalsPanel;

/**
 * The model for an {@link IntervalsPanel} object.
 *
 * @author Leopoldo Ghielmetti
 */
public interface IntervalsModelIfc {
  /** Identifier for observation of selected detail change. */
  String OBSERVE_SELECTED_DETAIL    = "SelectedDetail";
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
   * Returns all the known Pokémons.
   *
   * @return A {@link Collection} of {@link Pokemon}.
   */
  Collection<Pokemon> getAllPokemons();

  /**
   * Accessor.
   *
   * @return The selected candidate.
   */
  CandidateList getSelectedCandidate();

  /**
   * Accessor.
   *
   * @return The selected details.
   */
  CandidateDetail getSelectedDetail();

  /**
   * Sets the details.
   *
   * @param inSelectedDetail The new details.
   */
  void setSelectedDetails(CandidateDetail inSelectedDetail);
}
