package net.ghielmetti.ivcalc.pokedex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Type} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class TypeTest {
  /** Tests {@link Type} to complete the coverage. */
  @Test
  public void forCoverage() {
    assertEquals(Type.DRAGON, Type.valueOf(Type.DRAGON.name()));
  }

  /** Tests {@link Type#getColor()}. */
  @Test
  public void getColor_always_returnsAColor() {
    for (Type type : Type.values()) {
      assertNotNull(type.getColor());
    }
  }

  /** Tests {@link Type#getTypeName()}. */
  @Test
  public void getTypeName_always_returnsAString() {
    for (Type type : Type.values()) {
      assertNotNull(type.getTypeName());
    }
  }

  /** Initializes the tests. */
  @Before
  public void setUp() {
    Type.initialize();
  }
}
