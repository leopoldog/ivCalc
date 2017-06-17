package net.ghielmetti.ivcalc.tools;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.pokedex.Team;

/**
 * Stores the preferences for the Pokémon application.
 *
 * @author Leopoldo Ghielmetti
 */
public class PreferenceStore {
  private static final String RESOURCE_PREFERENCES   = "/preferences.properties";
  private static final Logger LOGGER                 = LoggerFactory.getLogger(PreferenceStore.class);
  private static final String PREFERENCE_TEAM_NAME   = "team.name";
  private static final String PREFERENCE_LANGUAGE    = "language";
  private static final String PREFERENCE_VIEW_STATE  = "view.state";
  private static final String PREFERENCE_VIEW_WIDTH  = "view.width";
  private static final String PREFERENCE_VIEW_HEIGHT = "view.height";
  private static final String PREFERENCE_VIEW_X      = "view.x";
  private static final String PREFERENCE_VIEW_Y      = "view.y";
  private static final String PREFERENCES_BASE_NAME;

  private static Preferences  appPreferences;

  static {
    Properties properties = new Properties();

    try (InputStream is = PreferenceStore.class.getResourceAsStream(RESOURCE_PREFERENCES)) {
      properties.load(is);
    } catch (IOException e) {
      LOGGER.error("Internal: Unable to read base name", e);
      System.exit(-1);
    }

    PREFERENCES_BASE_NAME = properties.getProperty("base.name");

    if (PREFERENCES_BASE_NAME == null || PREFERENCES_BASE_NAME.isEmpty()) {
      LOGGER.error("Internal: No base name defined");
      System.exit(-1);
    }
  }

  private PreferenceStore() {
    // nothing to do
  }

  /**
   * Gets the {@link Locale} for the application stored in the user profile.
   *
   * @return The {@link Locale}.
   */
  public static Locale getLocale() {
    String languageTag = getPreferences().get(PREFERENCE_LANGUAGE, null);

    if (languageTag != null) {
      return Locale.forLanguageTag(languageTag);
    }

    return Locale.getDefault();
  }

  /**
   * Gets the {@link Team} of the user stored in the user profile.
   *
   * @return The {@link Team}.
   */
  public static Team getTeam() {
    try {
      return Team.valueOf(getPreferences().get(PREFERENCE_TEAM_NAME, null));
    } catch (Exception e) {
      LOGGER.debug("Unable to read the team from the preferences, probably not defined.");
      return null;
    }
  }

  /**
   * Returns the bounds of the main view.
   *
   * @return The bounds.
   */
  public static Rectangle getViewBounds() {
    int x = getPreferences().getInt(PREFERENCE_VIEW_X, Integer.MAX_VALUE);
    int y = getPreferences().getInt(PREFERENCE_VIEW_Y, Integer.MAX_VALUE);
    int width = getPreferences().getInt(PREFERENCE_VIEW_WIDTH, 800);
    int height = getPreferences().getInt(PREFERENCE_VIEW_HEIGHT, 600);
    return new Rectangle(x, y, width, height);
  }

  /**
   * Returns the extended state of the view (maximised, minimised, ...).
   *
   * @return The extended state.
   */
  public static int getViewExtendedState() {
    return getPreferences().getInt(PREFERENCE_VIEW_STATE, 0);
  }

  /** Removes the store. */
  public static void removeStore() {
    if (appPreferences != null) {
      try {
        appPreferences.removeNode();
      } catch (BackingStoreException e) {
        LOGGER.error("Internal: Unable to remove the store", e);
      }

      appPreferences = null;
    }
  }

  /**
   * Saves the user preference's {@link Locale}.
   *
   * @param inLocale The new {@link Locale}.
   */
  public static void setLocale(final Locale inLocale) {
    getPreferences().put(PREFERENCE_LANGUAGE, inLocale.toLanguageTag());
  }

  /**
   * Saves the user preference's {@link Team}
   *
   * @param inTeam The {@link Team}.
   */
  public static void setTeam(final Team inTeam) {
    getPreferences().put(PREFERENCE_TEAM_NAME, inTeam.name());
  }

  /**
   * Saves the main view bounds.
   *
   * @param inBounds The bounds.
   */
  public static void setViewBounds(final Rectangle inBounds) {
    getPreferences().putInt(PREFERENCE_VIEW_WIDTH, (int) inBounds.getWidth());
    getPreferences().putInt(PREFERENCE_VIEW_HEIGHT, (int) inBounds.getHeight());
    getPreferences().putInt(PREFERENCE_VIEW_X, (int) inBounds.getX());
    getPreferences().putInt(PREFERENCE_VIEW_Y, (int) inBounds.getY());
  }

  /**
   * Saves the view extended state.
   *
   * @param inExtendedState The view extended state.
   */
  public static void setViewExtendedState(final int inExtendedState) {
    getPreferences().putInt(PREFERENCE_VIEW_STATE, inExtendedState);
  }

  private static Preferences getPreferences() {
    if (appPreferences == null) {
      appPreferences = Preferences.userRoot().node(PREFERENCES_BASE_NAME);
    }
    return appPreferences;
  }
}
