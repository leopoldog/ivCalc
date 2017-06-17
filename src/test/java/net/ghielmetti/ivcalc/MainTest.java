package net.ghielmetti.ivcalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Constructor;

import org.junit.Test;

/**
 * Tests for {@link Main} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class MainTest {
  /**
   * Tests {@link Main} constructor.
   *
   * @throws Exception Not expected.
   */
  @Test
  public void constructor_always_isPrivate() throws Exception {
    Constructor<?>[] constructors = Main.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);

    assertFalse(constructors[0].isAccessible());

    // For coverage only, we call the constructor!
    constructors[0].setAccessible(true);
    constructors[0].newInstance((Object[]) null);
  }

  /** Tests {@link Main#mainApplication(String[])}. */
  @Test
  public void mainApplication_fiveManyArguments_searchesForPokemons() {
    Main main = mock(Main.class);
    doCallRealMethod().when(main).mainApplication(any(String[].class));
    String[] arguments = new String[]{"a", "b", "c", "d", "e"};

    main.mainApplication(arguments);

    verify(main).searchPokemons(aryEq(arguments));
  }

  /** Tests {@link Main#mainApplication(String[])}. */
  @Test
  public void mainApplication_noArguments_startsGUI() {
    Main main = mock(Main.class);
    doCallRealMethod().when(main).mainApplication(any(String[].class));

    main.mainApplication(new String[]{});

    verify(main).startPokemonGUI();
  }

  /** Tests {@link Main#mainApplication(String[])}. */
  @Test
  public void mainApplication_tooFewArguments_displayHelp() {
    Main main = mock(Main.class);
    doCallRealMethod().when(main).mainApplication(any(String[].class));

    main.mainApplication(new String[]{"a"});

    verify(main).showHelp();
  }

  /** Tests {@link Main#mainApplication(String[])}. */
  @Test
  public void mainApplication_tooManyArguments_displayHelp() {
    Main main = mock(Main.class);
    doCallRealMethod().when(main).mainApplication(any(String[].class));

    main.mainApplication(new String[]{"a", "b", "c", "d", "e", "f"});

    verify(main).showHelp();
  }
}
