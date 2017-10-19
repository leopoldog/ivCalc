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
    int size;

    switch (Pokedex.getCurrentGeneration()) {
      case 1:
        size = 151;
        break;
      case 2:
        size = 251;
        break;
      case 3:
        size = 386;
        break;
      case 4:
        size = 493;
        break;
      case 5:
        size = 649;
        break;
      case 6:
        size = 721;
        break;
      case 7:
        size = 802;
        break;
      default:
        size = 0;
        break;
    }

    assertEquals(size, pokedex.getAllNames().size());
  }

  /** Tests {@link Pokedex#getAllNames()}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getAllNames_always_returnsAnUnmodifiableList() {
    pokedex.getAllNames().add("Name");
  }

  /** Tests {@link Pokedex#getAllPokemons()}. */
  @Test
  public void getAllPokemons_always_returnsAllTheKnownPokemons() {
    int size;

    switch (Pokedex.getCurrentGeneration()) {
      case 1:
        size = 151;
        break;
      case 2:
        size = 251;
        break;
      case 3:
        size = 386;
        break;
      case 4:
        size = 493;
        break;
      case 5:
        size = 649;
        break;
      case 6:
        size = 721;
        break;
      case 7:
        size = 802;
        break;
      default:
        size = 0;
        break;
    }

    assertEquals(size, pokedex.getAllPokemons().size());
  }

  /** Tests {@link Pokedex#getAllPokemons()}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getAllPokemons_always_returnsAnUnmodifiableList() {
    pokedex.getAllPokemons().add(new Pokemon(1234, "Name", 1, 2, 3, Type.DARK));
  }

  /** Tests {@link Pokedex#getAncestors(String)}. */
  @Test
  public void getAncestors_aPokemonWithManyGenerationsAncestors_returnsTheAncestors() {
    List<Pokemon> ancestors = pokedex.getAncestors("raichu");
    int size;

    switch (Pokedex.getCurrentGeneration()) {
      case 1:
        size = 1;
        break;
      default:
        size = 2;
        break;
    }

    assertEquals(size, ancestors.size());
  }

  /** Tests {@link Pokedex#getAncestors(String)}. */
  @Test
  public void getAncestors_aPokemonWithOneGenerationAncestors_returnsTheAncestors() {
    List<Pokemon> ancestors = pokedex.getAncestors("sandslash");
    assertEquals(1, ancestors.size());
  }

  /** Tests {@link Pokedex#getAncestors(String)}. */
  @Test
  public void getAncestors_aPokemonWithoutAncestors_returnsAnEmptyList() {
    List<Pokemon> ancestors = pokedex.getAncestors("ekans");
    assertTrue(ancestors.isEmpty());
  }

  /** Tests {@link Pokedex#getOffsprings(String)}. */
  @Test
  public void getOffsprings_aPokemonWithManyManyGenerationsOffsprings_returnsTheOffsprings() {
    List<Pokemon> offsprings = pokedex.getOffsprings("cleffa");
    int size;

    switch (Pokedex.getCurrentGeneration()) {
      case 1:
        size = 0;
        break;
      default:
        size = 2;
        break;
    }

    assertEquals(size, offsprings.size());
  }

  /** Tests {@link Pokedex#getOffsprings(String)}. */
  @Test
  public void getOffsprings_aPokemonWithManyOneGenerationOffsprings_returnsTheOffsprings() {
    List<Pokemon> offsprings = pokedex.getOffsprings("eevee");
    int size;

    switch (Pokedex.getCurrentGeneration()) {
      case 1:
        size = 3;
        break;
      case 2:
        size = 5;
        break;
      case 3:
        size = 5;
        break;
      case 4:
        size = 7;
        break;
      case 5:
        size = 7;
        break;
      case 6:
        size = 8;
        break;
      case 7:
        size = 8;
        break;
      default:
        size = 0;
        break;
    }

    assertEquals(size, offsprings.size());
  }

  /** Tests {@link Pokedex#getOffsprings(String)}. */
  @Test
  public void getOffsprings_aPokemonWithoutOffsprings_returnsAnEmptyList() {
    List<Pokemon> offsprings = pokedex.getOffsprings("raticate");
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

    if (Pokedex.getCurrentGeneration() >= 1) {
      assertTrue(names.size() >= 3);
      assertTrue(names.contains(pokedex.getPokemon("dragonair")));
      assertTrue(names.contains(pokedex.getPokemon("raichu")));
      assertTrue(names.contains(pokedex.getPokemon("clefairy")));
    }

    if (Pokedex.getCurrentGeneration() >= 2) {
      assertTrue(names.size() >= 6);
      assertTrue(names.contains(pokedex.getPokemon("aipom")));
      assertTrue(names.contains(pokedex.getPokemon("raikou")));
      assertTrue(names.contains(pokedex.getPokemon("remoraid")));
    }

    if (Pokedex.getCurrentGeneration() >= 3) {
      assertTrue(names.size() >= 12);
      assertTrue(names.contains(pokedex.getPokemon("wailord")));
      assertTrue(names.contains(pokedex.getPokemon("masquerain")));
      assertTrue(names.contains(pokedex.getPokemon("lairon")));
      assertTrue(names.contains(pokedex.getPokemon("taillow")));
      assertTrue(names.contains(pokedex.getPokemon("huntail")));
      assertTrue(names.contains(pokedex.getPokemon("wailmer")));
    }
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
