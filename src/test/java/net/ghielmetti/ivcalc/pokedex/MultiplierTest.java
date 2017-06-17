package net.ghielmetti.ivcalc.pokedex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link Multiplier} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class MultiplierTest {
  /** Tests {@link Multiplier#getInstance()}. */
  @Test
  public void getInstance_always_returnsTheSameMultiplier() {
    assertSame(Multiplier.getInstance(), Multiplier.getInstance());
  }

  /** Tests {@link Multiplier#getMultiplier(int)}. */
  @Test
  public void getMultiplier_anInvalidLevel_returnsNull() {
    assertEquals(Double.valueOf(0), Multiplier.getInstance().getMultiplier(0));
  }

  /** Tests {@link Multiplier#getMultiplier(int)}. */
  @Test
  public void getMultiplier_aValidLevel_returnsTheCorrespondingMultiplier() {
    assertEquals(new Double(0.4530599578D), Multiplier.getInstance().getMultiplier(23));
  }

  /** Tests {@link Multiplier#getMultipliers(int)}. */
  @Test
  public void getMultipliers_anInvalidStardustValue_returnsAnEmptyList() {
    assertEquals(0, Multiplier.getInstance().getMultipliers(0).size());
  }

  /** Tests {@link Multiplier#getMultipliers(int)}. */
  @Test
  public void getMultipliers_aValidStardustValue_returnsAListOfMultipliers() {
    assertEquals(4, Multiplier.getInstance().getMultipliers(3000).size());
  }

  /** Tests {@link Multiplier#getStardustList()}. */
  @Test
  public void getStardustList_always_returnsTheListOfAllValidStardustValues() {
    assertEquals(20, Multiplier.getInstance().getStardustList().size());
  }
}
