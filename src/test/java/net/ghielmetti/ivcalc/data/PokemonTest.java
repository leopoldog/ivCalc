package net.ghielmetti.ivcalc.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.Before;
import org.junit.Test;

import net.ghielmetti.ivcalc.pokedex.Type;

/**
 * Tests for {@link Pokemon} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonTest {
  private Pokemon pokemon;

  /** Tests {@link Pokemon#getIV(Limit, int, int, int)}. */
  @Test
  public void geIV_invalidPokemonInformations_returnsAnEmptyList() {
    assertEquals(0, pokemon.getIV(new Limit("4d-a"), 446, 53, 2500).getPossibleLevels().size());
    assertEquals(0, pokemon.getIV(new Limit("2d-d"), 504, 12, 4000).getPossibleLevels().size());
  }

  /** Tests {@link Pokemon#getIV(Limit, int, int, int)}. */
  @Test
  public void geIV_validPokemonInformations_returnsThePossibleIV() {
    CandidateList candidates = pokemon.getIV(new Limit("3b-d"), 446, 53, 2500);

    assertEquals(2, candidates.getPossibleLevels().size());
    assertEquals(Integer.valueOf(40), candidates.getPossibleLevels().get(0));
    assertEquals(Integer.valueOf(41), candidates.getPossibleLevels().get(1));
    assertEquals(new IVLevel(11, 13, 11), candidates.getIVLevels(Integer.valueOf(40)).get(0));
    assertEquals(new IVLevel(8, 13, 11), candidates.getIVLevels(Integer.valueOf(41)).get(0));
    assertEquals(new IVLevel(8, 14, 10), candidates.getIVLevels(Integer.valueOf(41)).get(1));
  }

  /** Tests {@link Pokemon#getMinMaxCP(int)}. */
  @Test
  public void geMinMaxCVP_validLevel_returnsTheMinAnDMaxCPForTheLevel() {
    assertEquals(ImmutablePair.of(Integer.valueOf(358), Integer.valueOf(475)), pokemon.getMinMaxCP(40));
  }

  /** Tests {@link Pokemon#getBaseValues()}. */
  @Test
  public void getBaseValues_always_returnsTheBaseValues() {
    assertEquals(ImmutableTriple.of(Integer.valueOf(116), Integer.valueOf(96), Integer.valueOf(78)), pokemon.getBaseValues());
  }

  /** Tests {@link Pokemon#getCPForLevel(int, IVLevel)}. */
  @Test
  public void getCPForLevel_validLevelAndIVLevel_returnsCPOfThePokemon() {
    assertEquals(446, pokemon.getCPForLevel(40, new IVLevel(11, 13, 11)));
  }

  /** Tests {@link Pokemon#getCPForStardust(int, IVLevel)}. */
  @Test
  public void getCPForStardust_always_returnsTheCPForThePokemon() {
    assertEquals(527, pokemon.getCPForStardust(4000, new IVLevel(11, 7, 6)).get(Integer.valueOf(50)).intValue());
  }

  /** Tests {@link Pokemon#getIcon()}. */
  @Test
  public void getIcon_aPokemonWithAnInvalidId_returnsNull() {
    assertNull(new Pokemon(12345, "Something", 1, 1, 1).getIcon());
  }

  /** Tests {@link Pokemon#getIcon()}. */
  @Test
  public void getIcon_aPokemonWithAValidId_returnsAnIcon() {
    assertNotNull(pokemon.getIcon());
    assertEquals(96, pokemon.getIcon().getIconWidth());
    assertEquals(96, pokemon.getIcon().getIconHeight());
  }

  /** Tests {@link Pokemon#getName()}. */
  @Test
  public void getName_always_returnsTheNameOfThePokemon() {
    assertEquals("Charmander", pokemon.getName());
  }

  /** Tests {@link Pokemon#getNumber()}. */
  @Test
  public void getNumber_always_returnsTheNumberOfThePokemon() {
    assertEquals(4, pokemon.getNumber());
  }

  /** Tests {@link Pokemon#getSmallIcon()}. */
  @Test
  public void getSmallIcon_aPokemonWithAValidId_returnsAnIcon() {
    assertNotNull(pokemon.getSmallIcon());
    assertEquals(32, pokemon.getSmallIcon().getIconWidth());
    assertEquals(32, pokemon.getSmallIcon().getIconHeight());
  }

  /** Tests {@link Pokemon#getTypes()}. */
  @Test
  public void getTypes_always_returnsTheTypesOfThePokemon() {
    assertEquals(Type.BUG, pokemon.getTypes()[0]);
  }

  /** Tests {@link Pokemon#getValues(int, IVLevel)}. */
  @Test
  public void getValues_validLevelAndIVLevel_returnsTheValuesForThePokemon() {
    assertEquals(ImmutableTriple.of(Integer.valueOf(75), Integer.valueOf(65), Integer.valueOf(53)), pokemon.getValues(40, new IVLevel(11, 13, 11)));
  }

  /** Tests {@link Pokemon#hashCode()}. */
  @Test
  public void hashCode_twoPokemonsDifferentName_returnsADifferentValue() {
    assertFalse(pokemon.hashCode() == new Pokemon(1, "Something", 1, 1, 1, Type.BUG).hashCode());
  }

  /** Tests {@link Pokemon#hashCode()}. */
  @Test
  public void hashCode_twoPokemonsWithSameName_returnsTheSameValue() {
    assertTrue(pokemon.hashCode() == new Pokemon(1, "Charmander", 1, 1, 1, Type.BUG).hashCode());
  }

  /** Initialize the test environment. */
  @Before
  public void setUp() {
    pokemon = new Pokemon(4, "Charmander", 116, 96, 78, Type.BUG, Type.DRAGON);
  }

  /** Tests {@link Pokemon#toString()}. */
  @Test
  public void toString_always_returnsTheName() {
    assertEquals("Charmander", pokemon.toString());
  }
}
