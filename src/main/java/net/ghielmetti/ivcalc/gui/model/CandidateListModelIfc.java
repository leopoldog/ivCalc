package net.ghielmetti.ivcalc.gui.model;

import java.util.List;
import java.util.Observer;

import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.gui.panel.CandidateListPanel;

/**
 * The model for a {@link CandidateListPanel} object.
 *
 * @author Leopoldo Ghielmetti
 */
public interface CandidateListModelIfc {
  /** Identifier for observation of candidate list change. */
  String OBSERVE_CANDIDATE_LIST = "CandidateList";
  /** Identifier for observation of half levels flag change. */
  String OBSERVE_HALF_LEVELS    = "HalfLevels";

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
   * Searches all the possible candidates with the specified characteristics.
   *
   * @return A {@link List} of matching candidates.
   */
  List<CandidateList> getCandidates();

  /**
   * Accessor.
   *
   * @return The selected candidate.
   */
  CandidateList getSelectedCandidate();

  /**
   * Accessor.
   *
   * @return <code>true</code> if we should display also the half levels.
   */
  boolean getShowHalfLevels();

  /**
   * Sets the candidate.
   *
   * @param inSelectedCandidates The new candidate.
   */
  void setSelectedCandidate(CandidateList inSelectedCandidates);

  /**
   * Sets the details as choosen by the user.
   *
   * @param inSelectedDetail The new details
   */
  void setSelectedDetails(CandidateDetail inSelectedDetail);

  /**
   * Setter.
   *
   * @param inShow <code>true</code> if we should display also the half levels.
   */
  void setShowHalfLevels(boolean inShow);
}
