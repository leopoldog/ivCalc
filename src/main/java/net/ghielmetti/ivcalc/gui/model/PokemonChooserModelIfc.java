package net.ghielmetti.ivcalc.gui.model;

import java.util.Collection;
import java.util.List;
import java.util.Observer;

import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.gui.panel.PokemonChooserPanel;

/**
 * The model for {@link PokemonChooserPanel}.
 *
 * @author Leopoldo Ghielmetti
 */
public interface PokemonChooserModelIfc {
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
   * Returns the list of {@link Pokemon}.
   *
   * @return The list.
   */
  List<Pokemon> getPokemons();

  /**
   * Sets the {@link Pokemon} list.
   *
   * @param inPokemonList The list of Pokemons.
   */
  void setPokemons(List<Pokemon> inPokemonList);
}
