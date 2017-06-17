package net.ghielmetti.ivcalc.gui.model;

import net.ghielmetti.ivcalc.data.IVLevel;

/**
 * A {@link GoodnessModelIfc} allows to check if a Pokémon is a good one or not.
 *
 * @author Leopoldo Ghielmetti
 */
public interface GoodnessModelIfc {
  /**
   * Returns if this level is a good or bad Pokémon.
   *
   * @param inPokemonName The Pokémon name.
   * @param inIvLevel The {@link IVLevel} to check.
   * @return <code>true</code> if it's good.
   */
  boolean isGood(String inPokemonName, IVLevel inIvLevel);
}
