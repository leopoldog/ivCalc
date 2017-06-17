package net.ghielmetti.ivcalc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.After;
import org.junit.Test;

import net.ghielmetti.ivcalc.pokedex.Team;

/**
 * Tests for {@link PreferenceStore} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class PreferenceStoreTest {
  /**
   * Tests {@link PreferenceStore} constructor.
   *
   * @throws Exception Not expected.
   */
  @Test
  public void constructor_always_isPrivate() throws Exception {
    Constructor<?>[] constructors = PreferenceStore.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);

    assertFalse(constructors[0].isAccessible());

    // For coverage only, we call the constructor!
    constructors[0].setAccessible(true);
    constructors[0].newInstance((Object[]) null);
  }

  /** Tests {@link PreferenceStore#getLocale()}. */
  @Test
  public void getLocale_aLanguageDefined_returnsTheDefinedLanguae() {
    PreferenceStore.setLocale(Locale.ENGLISH);
    assertEquals(Locale.ENGLISH, PreferenceStore.getLocale());

    // A second time just to ensure that the first one is not already the default :-)
    PreferenceStore.setLocale(Locale.FRANCE);
    assertEquals(Locale.FRANCE, PreferenceStore.getLocale());
  }

  /** Tests {@link PreferenceStore#getLocale()}. */
  @Test
  public void getLocale_noLanguageDefined_returnsDefault() {
    assertEquals(Locale.getDefault(), PreferenceStore.getLocale());
  }

  /** Tests {@link PreferenceStore#getTeam()}. */
  @Test
  public void getTeam_aTeamDefined_returnsTheTeam() {
    PreferenceStore.setTeam(Team.INSTINCT);

    assertEquals(Team.INSTINCT, PreferenceStore.getTeam());
  }

  /** Tests {@link PreferenceStore#getTeam()}. */
  @Test
  public void getTeam_noTeamDefined_returnsNull() {
    assertNull(PreferenceStore.getTeam());
  }

  /** Tests {@link PreferenceStore#getViewBounds()}. */
  @Test
  public void getViewBounds_noBoundsDefined_returnsDefault() {
    Rectangle r = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 800, 600);

    assertEquals(r, PreferenceStore.getViewBounds());
  }

  /** Tests {@link PreferenceStore#getViewBounds()}. */
  @Test
  public void getViewBounds_theBoundsAreDefined_returnsTheBounds() {
    Rectangle r = new Rectangle(10, 20, 30, 40);
    PreferenceStore.setViewBounds(r);

    assertEquals(r, PreferenceStore.getViewBounds());
  }

  /** Tests {@link PreferenceStore#getViewExtendedState()}. */
  @Test
  public void getViewExtendedState_noExtendedStatesDefined_returnsZero() {
    assertEquals(0, PreferenceStore.getViewExtendedState());
  }

  /** Tests {@link PreferenceStore#getViewExtendedState()}. */
  @Test
  public void getViewExtendedState_theExtendedStatesAreDefined_returnsTheValue() {
    PreferenceStore.setViewExtendedState(1234);

    assertEquals(1234, PreferenceStore.getViewExtendedState());
  }

  /** Tests {@link PreferenceStore#removeStore()}. */
  @Test
  public void removeStore_always_removesTheStore() {
    PreferenceStore.setTeam(Team.MYSTIC);
    assertEquals(Team.MYSTIC, PreferenceStore.getTeam());

    PreferenceStore.removeStore();

    assertNull(PreferenceStore.getTeam());
  }

  /**
   * Tests {@link PreferenceStore#removeStore()}.
   *
   * @throws Exception not Expected.
   */
  @Test
  public void removeStore_inCaseOfException_ignoresTheException() throws Exception {
    Preferences p = mock(Preferences.class);
    doThrow(new BackingStoreException("Test")).when(p).removeNode();
    Field f = PreferenceStore.class.getDeclaredField("appPreferences");
    f.setAccessible(true);
    f.set(null, p);

    PreferenceStore.removeStore();
  }

  /** Resets the test environment. */
  @After
  public void tearDown() {
    PreferenceStore.removeStore();
  }
}
