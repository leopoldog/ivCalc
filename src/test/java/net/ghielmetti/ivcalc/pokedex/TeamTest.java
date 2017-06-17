package net.ghielmetti.ivcalc.pokedex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Team} class.
 *
 * @author Leopoldo Ghielmetti
 */
public class TeamTest {
  /** Tests {@link Team} to complete the coverage. */
  @Test
  public void forCoverage() {
    assertEquals(Team.VALOR, Team.valueOf(Team.VALOR.name()));
  }

  /** Tests {@link Team#getColor()}. */
  @Test
  public void getColor_always_returnsAColor() {
    for (Team team : Team.values()) {
      assertNotNull(team.getColor());
    }
  }

  /** Tests {@link Team#getLeaderName()}. */
  @Test
  public void getLeaderName_always_returnsAString() {
    for (Team team : Team.values()) {
      assertNotNull(team.getLeaderName());
    }
  }

  /** Tests {@link Team#getStatsLabel(int)}. */
  @Test
  public void getStatsLabel_anInvalidStatValue_returnsAnErrorString() {
    assertEquals("Error: stat 0", Team.INSTINCT.getStatsLabel(0));
  }

  /** Tests {@link Team#getStatsLabel(int)}. */
  @Test
  public void getStatsLabel_aValidStatValue_returnsAString() {
    assertNotNull(Team.MYSTIC.getStatsLabel(1));
  }

  /** Tests {@link Team#getStatsLabel(int)}. */
  @Test(expected = RuntimeException.class)
  public void getStatsLabel_notInitializedTeams_throwsAnException() {
    assertNotNull(Team.VALOR.getStatsLabel(0));
  }

  /** Tests {@link Team#getTeamName()}. */
  @Test
  public void getTeamName_always_returnsAString() {
    for (Team team : Team.values()) {
      assertNotNull(team.getTeamName());
    }
  }

  /** Tests {@link Team#getTotalLabel(int)}. */
  @Test
  public void getTotalLabel_anInvalidTotalValue_returnsAnErrorString() {
    assertEquals("Error: total 0", Team.INSTINCT.getTotalLabel(0));
  }

  /** Tests {@link Team#getTotalLabel(int)}. */
  @Test
  public void getTotalLabel_aValidTotalValue_returnsAString() {
    assertNotNull(Team.MYSTIC.getTotalLabel(1));
  }

  /** Tests {@link Team#getTotalLabel(int)}. */
  @Test(expected = RuntimeException.class)
  public void getTotalLabel_notInitializedTeams_throwsAnException() {
    assertNotNull(Team.VALOR.getTotalLabel(0));
  }

  /** Initializes the tests. */
  @Before
  public void setUp() {
    // Sets the Locale to something I'm using as default
    Locale.setDefault(Locale.ENGLISH);
    Team.MYSTIC.initialize();
    Team.INSTINCT.initialize();
  }
}
