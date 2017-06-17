package net.ghielmetti.ivcalc.pokedex;

import java.awt.Color;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Pokémon teams.
 *
 * @author Leopoldo Ghielmetti
 */
public enum Team {
  /** Team Valor */
  VALOR("Valor", "Candela", new Color(219, 0, 0)),
  /** Team mystic */
  MYSTIC("Mystic", "Blanche", new Color(0, 0, 219)),
  /** Team Instinct */
  INSTINCT("Instinct", "Spark", new Color(219, 219, 0));

  private ResourceBundle properties;
  private final String   teamName;
  private final String   leaderName;
  private final Color    color;

  private Team(final String inTeamName, final String inLeaderName, final Color inColor) {
    teamName = inTeamName;
    color = inColor;
    leaderName = inLeaderName;
  }

  /**
   * Returns the Team {@link Color}.
   *
   * @return The {@link Color}.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the leader name.
   *
   * @return The name {@link String}.
   */
  public String getLeaderName() {
    return leaderName;
  }

  /**
   * Returns the translation for the stats label.<br>
   * The stats can be 0, 1, 2 or 3.
   *
   * @param inStats The stats number.
   * @return The corresponding label.
   */
  public String getStatsLabel(final int inStats) {
    if (properties == null) {
      throw new RuntimeException("Internal: Team " + teamName + " is not initialized, call initialize first!");
    }
    try {
      return properties.getString("stats.level" + inStats);
    } catch (MissingResourceException e) {
      return "Error: stat " + inStats;
    }
  }

  /**
   * Returns the team name.
   *
   * @return The name.
   */
  public String getTeamName() {
    return teamName;
  }

  /**
   * Returns the translation for the total label.<br>
   * The total can be 0, 1, 2 or 3.
   *
   * @param inTotal The total number.
   * @return The corresponding label.
   */
  public String getTotalLabel(final int inTotal) {
    if (properties == null) {
      throw new RuntimeException("Internal: Team " + teamName + " is not initialized, call initialize first!");
    }
    try {
      return properties.getString("total.level" + inTotal);
    } catch (MissingResourceException e) {
      return "Error: total " + inTotal;
    }
  }

  /** Initialize the translation table. */
  public void initialize() {
    properties = ResourceBundle.getBundle(name().toLowerCase());
  }
}
