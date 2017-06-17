package net.ghielmetti.ivcalc.pokedex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;

import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.data.Limit;

/**
 * Tests for {@link IVMap} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class IVMapTest {
  /**
   * Tests {@link IVMap} constructor.
   *
   * @throws Exception Not expected.
   */
  @Test
  public void constructor_always_isPrivate() throws Exception {
    Constructor<?>[] constructors = IVMap.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);

    assertFalse(constructors[0].isAccessible());

    // For coverage only, we call the constructor!
    constructors[0].setAccessible(true);
    constructors[0].newInstance((Object[]) null);
  }

  /** Tests {@link IVMap#getIVLevels(Limit)}. */
  @Test
  public void getIVLevels_anInvalidLimit_returnsAnEmptyList() {
    assertTrue(IVMap.getIVLevels(new Limit("4d-had")).isEmpty());
  }

  /** Tests {@link IVMap#getIVLevels(Limit)}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getIVLevels_anInvalidLimit_returnsAnUnmodifiableList() {
    IVMap.getIVLevels(new Limit("4d-had")).add(new IVLevel(1, 2, 3));
  }

  /** Tests {@link IVMap#getIVLevels(Limit)}. */
  @Test
  public void getIVLevels_aValidLimit_returnsANotEmptyList() {
    assertFalse(IVMap.getIVLevels(new Limit("3b-ah")).isEmpty());
  }

  /** Tests {@link IVMap#getIVLevels(Limit)}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getIVLevels_aValidLimit_returnsAnUnmodifiableList() {
    IVMap.getIVLevels(new Limit("3b-ah")).add(new IVLevel(1, 2, 3));
  }

  /** Tests {@link IVMap#getLimits()}. */
  @Test
  public void getLimits_always_returnsANotEmptyList() {
    assertFalse(IVMap.getLimits().isEmpty());
  }

  /** Tests {@link IVMap#getLimits()}. */
  @Test(expected = UnsupportedOperationException.class)
  public void getLimits_always_returnsAnUnmodifiableList() {
    IVMap.getLimits().add(new Limit("4d-had"));
  }
}
