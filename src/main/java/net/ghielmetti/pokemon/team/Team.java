package net.ghielmetti.pokemon.team;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Team {
  VALOR("Valor (Leader: Candela)", Color.RED), //
  MYSTIC("Mystic (Leader: Blanche)", Color.BLUE), //
  INSTINCT("Instinct (Leader: Spark)", Color.YELLOW);

  private final Properties properties = new Properties();
  private final String     teamName;
  private final Color      color;

  private Team(final String inTeamName, final Color inColor) {
    teamName = inTeamName;
    color = inColor;

    try (InputStream is = Team.class.getResourceAsStream("/" + name().toLowerCase() + ".properties")) {
      properties.load(is);
    } catch (IOException e) {
      // should never happen
    }
  }

  public Color getColor() {
    return color;
  }

  public String getStatsLabel(final int inTotal) {
    String label = (String) properties.get("stats.level" + inTotal);
    if (label == null) {
      return "Error: total " + inTotal;
    }
    return label;
  }

  public String getTeamName() {
    return teamName;
  }

  public String getTotalLabel(final int inStats) {
    String label = (String) properties.get("total.level" + inStats);
    if (label == null) {
      return "Error: stats " + inStats;
    }
    return label;
  }
}
