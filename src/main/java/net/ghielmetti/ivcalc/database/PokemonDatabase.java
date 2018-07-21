package net.ghielmetti.ivcalc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.data.GoodnessChecker;
import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.data.Pokemon;

/**
 * Class that allows the management of the database of ths application.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonDatabase {
  private static final Logger          LOGGER                = LoggerFactory.getLogger(PokemonDatabase.class);
  private static final String          CREATE_TABLE_VERSION  = "CREATE TABLE IF NOT EXISTS version (version integer not null);";
  private static final String          CREATE_TABLE_NAMES    = "CREATE TABLE IF NOT EXISTS names (id integer primary key autoincrement, name text not null, pokemon integer not null, level integer not null, strength integer not null, attack boolean not null, defense boolean not null, hp boolean not null);";
  private static final String          CREATE_INDEX_NAMES    = "CREATE UNIQUE INDEX IF NOT EXISTS u_names ON names (name, pokemon);";
  private static final String          CREATE_TABLE_CODES    = "CREATE TABLE IF NOT EXISTS codes (id integer primary key autoincrement, names_id integer not null, level integer not null, ivAttack integer not null, ivDefense integer not null, ivStamina integer not null);";
  private static final String          CREATE_TABLE_CHECKERS = "CREATE TABLE IF NOT EXISTS checkers (id integer primary key autoincrement, name text not null, minTotal integer not null, minAttack integer not null, minDefense integer not null, minStamina integer not null);";
  private static final String          CREATE_INDEX_CHECKERS = "CREATE UNIQUE INDEX IF NOT EXISTS u_checkers ON checkers (name);";
  private static final PokemonDatabase database              = new PokemonDatabase();
  private final String                 name                  = System.getProperty("user.home") + "/.ivcalc.db";
  private int                          dbVersion;
  private Connection                   connection;

  private PokemonDatabase() {
    // nothing to do
  }

  /** Close the database connection. */
  public static void close() {
    if (database.connection != null) {
      try {
        database.connection.close();
      } catch (SQLException e) {
        LOGGER.debug("Unable to close database", e);
      }
    }

    database.connection = null;
  }

  /**
   * Returns the instance of this database.
   *
   * @return The database instance.
   */
  public static PokemonDatabase getInstance() {
    database.open();
    return database;
  }

  /** Creates the structure for a V1 database. */
  public void createDbStructureV1() {
    dbVersion = 1;
    try (Statement stmt = connection.createStatement()) {
      stmt.execute(CREATE_TABLE_VERSION);
      stmt.execute(CREATE_TABLE_NAMES);
      stmt.execute(CREATE_TABLE_CODES);
      stmt.execute(CREATE_TABLE_CHECKERS);

      stmt.execute(CREATE_INDEX_NAMES);
      stmt.execute(CREATE_INDEX_CHECKERS);

      stmt.execute("insert into version values (1)");
    } catch (SQLException e) {
      LOGGER.error("Unable to create database", e);
    }
  }

  /**
   * Returns the version of this DB.
   *
   * @return The version number.
   */
  public int getDbVersion() {
    if (dbVersion == 0) {
      try (Statement stmt = connection.createStatement(); //
          ResultSet rs = stmt.executeQuery("select version from version")) {
        if (rs.next()) {
          dbVersion = rs.getInt(1);
        }
      } catch (SQLException e) {
        LOGGER.debug("Unable to get dbVersion", e);
      }
    }

    return dbVersion;
  }

  /**
   * Gets the saved candidates for a given Pokémon and his ancestors.
   *
   * @param inPokemon The Pokémon type (it searches also for all the possible ancestors).
   * @param inName The name of the saved instance.
   * @param inLimit The Limit of the searched Pokémon.
   * @return The {@link CandidateList} retrieved from the database.
   */
  public CandidateList getSavedCandidates(final Pokemon inPokemon, final String inName, final Limit inLimit) {
    CandidateList list = new CandidateList(inPokemon, inLimit);

    try (PreparedStatement stmt = connection.prepareStatement("select level, ivAttack, ivDefense, ivStamina from codes where names_id=(select id from names where name=? and pokemon=?)")) {
      stmt.setString(1, inName);
      stmt.setInt(2, inPokemon.getNumber());

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          int level = rs.getInt(1);
          int ivAttack = rs.getInt(2);
          int ivDefense = rs.getInt(3);
          int ivStamina = rs.getInt(4);

          list.addIVLevel(Integer.valueOf(level), new IVLevel(ivAttack, ivDefense, ivStamina));
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Unable to read candidates", e);
    }

    return list;
  }

  /**
   * List all saved {@link GoodnessChecker}.
   *
   * @return A {@link List} of {@link GoodnessChecker}.
   */
  public List<GoodnessChecker> listGoodnessCheckers() {
    List<GoodnessChecker> list = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement("select name, minTotal, minAttack, minDefense, minStamina from checkers"); //
        ResultSet rs = stmt.executeQuery();) {
      while (rs.next()) {
        list.add(new GoodnessChecker(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5)));
      }
    } catch (SQLException e) {
      LOGGER.error("Unable to list checkers", e);
    }

    return list;
  }

  /**
   * Lists all the names of the stored candidates for the given Pokémon (and his ancestors).
   *
   * @param inPokemon The Pokémon to search.
   * @param inLimit The Limit of this Pokémon.
   * @return A {@link List} of names.
   */
  public List<String> listSavedCandidates(final Pokemon inPokemon, final Limit inLimit) {
    List<String> list = new ArrayList<>();

    if (inPokemon != null && inLimit != null) {
      try (PreparedStatement stmt = connection.prepareStatement("select name from names where pokemon=? and level=? and strength=? and attack=? and defense=? and hp=?")) {
        stmt.setInt(1, inPokemon.getNumber());
        stmt.setInt(2, inLimit.getLevel());
        stmt.setInt(3, inLimit.getStrength());
        stmt.setBoolean(4, inLimit.isAttack());
        stmt.setBoolean(5, inLimit.isDefense());
        stmt.setBoolean(6, inLimit.isHP());

        try (ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            list.add(rs.getString(1));
          }
        }
      } catch (SQLException e) {
        LOGGER.error("Unable to retrieve the pokemon names", e);
      }
    }

    return list;
  }

  /** Open the database connection. */
  public void open() {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection("jdbc:sqlite:" + name);

        if (getDbVersion() == 0) {
          createDbStructureV1();
        }
      } catch (SQLException e) {
        LOGGER.error("Unable to open database file \"{}\"", name, e);
      }
    }
  }

  /**
   * Removes a {@link GoodnessChecker} with the specified name.
   *
   * @param inName The name of the {@link GoodnessChecker} to remove.
   */
  public void removeGoodnessChecker(final String inName) {
    try (PreparedStatement stmt = connection.prepareStatement("delete from checkers where name=?")) {
      stmt.setString(1, inName);
      stmt.execute();
    } catch (SQLException e) {
      LOGGER.error("Unable to remove the checker \"{}\"", inName, e);
    }
  }

  /**
   * Removes the specified saved candidate.
   *
   * @param inPokemon The Pokémon to delete (it also deletes all ancestors Pokémon).
   * @param inName The name to delete.
   */
  public void removeSavedCandidates(final Pokemon inPokemon, final String inName) {
    try (PreparedStatement stmt1 = connection.prepareStatement("delete from codes where names_id=(select id from names where name=? and pokemon=?)")) {
      stmt1.setString(1, inName);
      stmt1.setInt(2, inPokemon.getNumber());
      stmt1.execute();

      try (PreparedStatement stmt2 = connection.prepareStatement("delete from names where name=? and pokemon=?")) {
        stmt2.setString(1, inName);
        stmt2.setInt(2, inPokemon.getNumber());
        stmt2.execute();
      }
    } catch (SQLException e) {
      LOGGER.error("Unable to remove the candidate", e);
    }
  }

  /**
   * Save the Pokémon candidates with the specified name.
   *
   * @param inCandidates The {@link CandidateList} to save.
   * @param inName The same to use.
   */
  public void saveCandidates(final CandidateList inCandidates, final String inName) {
    try {
      int index = saveName(inCandidates.getLimit(), inName, inCandidates.getPokemon().getNumber());

      try (PreparedStatement stmt = connection.prepareStatement("delete from codes where names_id=?")) {
        stmt.setInt(1, index);
        stmt.execute();
      }

      try (PreparedStatement stmt = connection.prepareStatement("insert into codes (names_id, level, ivAttack, ivDefense, ivStamina) values (?,?,?,?,?)")) {
        for (Integer level : inCandidates.getPossibleLevels()) {
          for (IVLevel ivLevel : inCandidates.getIVLevels(level)) {
            stmt.setInt(1, index);
            stmt.setInt(2, level.intValue());
            stmt.setInt(3, ivLevel.getAttack());
            stmt.setInt(4, ivLevel.getDefense());
            stmt.setInt(5, ivLevel.getStamina());
            stmt.addBatch();
          }
        }

        stmt.executeBatch();
      }
    } catch (SQLException e) {
      LOGGER.error("Unable to save the candidate", e);
    }
  }

  /**
   * Saves a {@link GoodnessChecker}.
   *
   * @param inChecker The {@link GoodnessChecker} to save.
   */
  public void saveGoodnessChecker(final GoodnessChecker inChecker) {
    try {
      try (PreparedStatement stmt = connection.prepareStatement("update checkers set minTotal=?, minAttack=?, minDefense=?, minStamina=? where name=?")) {
        stmt.setInt(1, inChecker.getMinTotal());
        stmt.setInt(2, inChecker.getMinAttack());
        stmt.setInt(3, inChecker.getMinDefense());
        stmt.setInt(4, inChecker.getMinStamina());
        stmt.setString(5, inChecker.getName());

        if (stmt.executeUpdate() == 0) {
          // No updates made, try insert
          try (PreparedStatement stmt1 = connection.prepareStatement("insert into checkers (minTotal, minAttack, minDefense, minStamina, name) values (?,?,?,?,?)")) {
            stmt1.setInt(1, inChecker.getMinTotal());
            stmt1.setInt(2, inChecker.getMinAttack());
            stmt1.setInt(3, inChecker.getMinDefense());
            stmt1.setInt(4, inChecker.getMinStamina());
            stmt1.setString(5, inChecker.getName());
            stmt1.execute();
          }
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Unable to save the checker \"{}\"", inChecker.getName(), e);
    }
  }

  private int saveName(final Limit inLimit, final String inName, final int inNumber) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement("update names set level=?, strength=?, attack=?, defense=?, hp=? where name=? and pokemon=?")) {
      stmt.setInt(1, inLimit.getLevel());
      stmt.setInt(2, inLimit.getStrength());
      stmt.setBoolean(3, inLimit.isAttack());
      stmt.setBoolean(4, inLimit.isDefense());
      stmt.setBoolean(5, inLimit.isHP());
      stmt.setString(6, inName);
      stmt.setInt(7, inNumber);

      if (stmt.executeUpdate() == 0) {
        try (PreparedStatement stmt1 = connection.prepareStatement("insert into names (level, strength, attack, defense, hp, name, pokemon) values (?,?,?,?,?,?,?)")) {
          stmt1.setInt(1, inLimit.getLevel());
          stmt1.setInt(2, inLimit.getStrength());
          stmt1.setBoolean(3, inLimit.isAttack());
          stmt1.setBoolean(4, inLimit.isDefense());
          stmt1.setBoolean(5, inLimit.isHP());
          stmt1.setString(6, inName);
          stmt1.setInt(7, inNumber);
          stmt1.execute();
        }
      }
    }

    try (PreparedStatement stmt = connection.prepareStatement("select id from names where name=? and pokemon=?")) {
      stmt.setString(1, inName);
      stmt.setInt(2, inNumber);

      try (ResultSet rs = stmt.executeQuery()) {
        rs.next();
        return rs.getInt(1);
      }
    }
  }
}
