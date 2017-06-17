package net.ghielmetti.ivcalc.pokedex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.ghielmetti.ivcalc.data.Pokemon;

/**
 * Tests for {@link Pokedex} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokedexTest {
  private Pokedex pokedex;

  /** Tests {@link Pokedex#Pokedex()}. */
  @Test
  public void constructor_always_createsAnObject() {
    assertNotNull(pokedex);
  }

  /** Tests {@link Pokedex#getAllNames()}. */
  @Test
  public void getAllNames_always_returnsAllTheKnownPokemonNames() {
    assertEquals(251, pokedex.getAllNames().size());
  }

  /** Tests {@link Pokedex#getAllNames()}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getAllNames_always_returnsAnUnmodifiableList() {
    pokedex.getAllNames().add("Name");
  }

  /** Tests {@link Pokedex#getAllPokemons()}. */
  @Test
  public void getAllPokemons_always_returnsAllTheKnownPokemons() {
    assertEquals(251, pokedex.getAllPokemons().size());
  }

  /** Tests {@link Pokedex#getAllPokemons()}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getAllPokemons_always_returnsAnUnmodifiableList() {
    pokedex.getAllPokemons().add(new Pokemon(1234, "Name", 1, 2, 3, Type.DARK));
  }

  /** Tests {@link Pokedex#getAncestors(Pokemon)}. */
  @Test
  public void getAncestors_aPokemonWithManyGenerationsAncestors_returnsTheAncestors() {
    List<Pokemon> ancestors = pokedex.getAncestors(pokedex.getPokemon("raichu"));
    assertEquals(2, ancestors.size());
  }

  /** Tests {@link Pokedex#getAncestors(Pokemon)}. */
  @Test
  public void getAncestors_aPokemonWithOneGenerationAncestors_returnsTheAncestors() {
    List<Pokemon> ancestors = pokedex.getAncestors(pokedex.getPokemon("sandslash"));
    assertEquals(1, ancestors.size());
  }

  /** Tests {@link Pokedex#getAncestors(Pokemon)}. */
  @Test
  public void getAncestors_aPokemonWithoutAncestors_returnsAnEmptyList() {
    List<Pokemon> ancestors = pokedex.getAncestors(pokedex.getPokemon("ekans"));
    assertTrue(ancestors.isEmpty());
  }

  /** Tests {@link Pokedex#getOffsprings(Pokemon)}. */
  @Test
  public void getOffsprings_aPokemonWithManyManyGenerationsOffsprings_returnsTheOffsprings() {
    List<Pokemon> offsprings = pokedex.getOffsprings(pokedex.getPokemon("cleffa"));
    assertEquals(2, offsprings.size());
  }

  /** Tests {@link Pokedex#getOffsprings(Pokemon)}. */
  @Test
  public void getOffsprings_aPokemonWithManyOneGenerationOffsprings_returnsTheOffsprings() {
    List<Pokemon> offsprings = pokedex.getOffsprings(pokedex.getPokemon("eevee"));
    assertEquals(5, offsprings.size());
  }

  /** Tests {@link Pokedex#getOffsprings(Pokemon)}. */
  @Test
  public void getOffsprings_aPokemonWithoutOffsprings_returnsAnEmptyList() {
    List<Pokemon> offsprings = pokedex.getOffsprings(pokedex.getPokemon("raticate"));
    assertTrue(offsprings.isEmpty());
  }

  /** Tests {@link Pokedex#getPokemon(String)}. */
  @Test
  public void getPokemon_aKnownPokemon_returnsTheObject() {
    assertEquals("Pikachu", pokedex.getPokemon("pIkAcHu").getName());
  }

  /** Tests {@link Pokedex#getPokemon(String)}. */
  @Test
  public void getPokemon_anUnknownPokemon_returnsNull() {
    assertNull(pokedex.getPokemon("UNKNOWN"));
  }

  /** Tests {@link Pokedex#getPokemonsFromPartialName(String)}. */
  @Test
  public void getPokemonsFromPartialName_aSubstring_returnsTheListOfPokemonsMatchingIgnoringCase() {
    List<Pokemon> names = pokedex.getPokemonsFromPartialName("AI");
    assertEquals(6, names.size());
    assertTrue(names.contains(pokedex.getPokemon("dragonair")));
    assertTrue(names.contains(pokedex.getPokemon("raichu")));
    assertTrue(names.contains(pokedex.getPokemon("aipom")));
    assertTrue(names.contains(pokedex.getPokemon("raikou")));
    assertTrue(names.contains(pokedex.getPokemon("clefairy")));
    assertTrue(names.contains(pokedex.getPokemon("remoraid")));
  }

  /** Initializes the tests. */
  @Before
  public void setUp() {
    pokedex = new Pokedex();
  }

  /** Tests {@link Pokedex#toString()}. */
  @Test
  public void toString_always_returnsAString() {
    assertNotNull(pokedex.toString());
  }
}
