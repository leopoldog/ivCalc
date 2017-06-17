package net.ghielmetti.ivcalc.gui.model;

import java.awt.Rectangle;

import net.ghielmetti.ivcalc.gui.PokemonView;

/**
 * The model for a {@link PokemonView} object.
 *
 * @author Leopoldo Ghielmetti
 */
public interface PokemonModelIfc extends AppraiseModelIfc, ValuesModelIfc, PokemonChooserModelIfc, CandidateListModelIfc, CandidateDetailsModelIfc, IntervalsModelIfc {
  /** Identifier for observation of CP change. */
  String OBSERVE_CP                 = "CP";
  /** Identifier for observation of HP change. */
  String OBSERVE_HP                 = "HP";
  /** Identifier for observation of SD change. */
  String OBSERVE_SD                 = "SD";
  /** Identifier for observation of Limit change. */
  String OBSERVE_LIMIT              = "Limit";
  /** Identifier for observation of Pokemons change. */
  String OBSERVE_POKEMONS           = "Pokemons";
  /** Identifier for observation of selected candidate change. */
  String OBSERVE_SELECTED_CANDIDATE = "SelectedCandidate";
  /** Identifier for observation of Team change. */
  String OBSERVE_TEAM               = "Team";

  /**
   * Reads from the persistence the bounds of the window.
   *
   * @return The bounds.
   */
  Rectangle getViewBounds();

  /**
   * Reads from the persistence the extended state of the window.
   *
   * @return The extended state.
   */
  int getViewExtendedState();

  /**
   * Stores the view bounds.
   *
   * @param inBounds The Window bounds.
   */
  void setViewBounds(Rectangle inBounds);

  /**
   * Stores the view extended state.
   *
   * @param inExtendedState The Window extra state.
   */
  void setViewExtendedState(int inExtendedState);
}
