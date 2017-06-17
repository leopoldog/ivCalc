package net.ghielmetti.ivcalc.pokedex;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

/**
 * This class stores all the level multipliers. <br>
 * A Pokémon CP is defined via a multiplier that is use to compute the CP for a specific level. These multipliers can be
 * computed via a complex formula or simply looked up in a table. I've choose this last method because it allows to fine
 * tune the multipliers in case they are changed.
 *
 * @author Leopoldo Ghielmetti
 */
public class Multiplier {
  private static final String                                RESOURCE_MULTIPLIERS = "/multipliers.csv";
  private static final Logger                                LOGGER               = LoggerFactory.getLogger(Multiplier.class);
  private static Multiplier                                  instance;
  private Map<Integer, List<ImmutablePair<Integer, Double>>> valuesBySD           = new HashMap<>();
  private Map<Integer, Double>                               valuesByLevel        = new HashMap<>();

  /** This private constructor reads the configuration file and stores all the multipliers in a table. */
  private Multiplier() {
    try (InputStream is = getClass().getResourceAsStream(RESOURCE_MULTIPLIERS); //
        InputStreamReader isr = new InputStreamReader(is); //
        CSVReader reader = new CSVReader(isr, '\t');) {
      String[] line;
      while ((line = reader.readNext()) != null) {
        Integer stardust = Integer.valueOf(line[2]);
        List<ImmutablePair<Integer, Double>> list = valuesBySD.get(stardust);
        if (list == null) {
          list = new ArrayList<>();
          valuesBySD.put(stardust, list);
        }
        Integer level = Integer.valueOf((int) (Float.parseFloat(line[0]) * 2.0));
        Double multiplier = Double.valueOf(line[1]);
        valuesByLevel.put(level, multiplier);
        list.add(ImmutablePair.of(level, multiplier));
      }
    } catch (Exception e) {
      LOGGER.error("Unable to instantiate the Multiplier", e);
    }
  }

  /**
   * Returns the instance of this {@link Multiplier}
   *
   * @return The {@link Multiplier}
   */
  public static Multiplier getInstance() {
    if (instance == null) {
      instance = new Multiplier();
    }
    return instance;
  }

  /**
   * Returns the multiplier for the level.
   *
   * @param inLevel The level
   * @return The multiplier
   */
  public Double getMultiplier(final int inLevel) {
    Double value = valuesByLevel.get(Integer.valueOf(inLevel));
    return value == null ? Double.valueOf(0) : value;
  }

  /**
   * Returns all the possible multipliers for the given stardust.<br>
   * A stardust value matches more than one level, so this method returns a List.
   *
   * @param inStardust The stardust
   * @return The list of {@link ImmutablePair} with the level and the multiplier
   */
  public List<ImmutablePair<Integer, Double>> getMultipliers(final int inStardust) {
    List<ImmutablePair<Integer, Double>> list = valuesBySD.get(Integer.valueOf(inStardust));
    return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
  }

  /**
   * Returns all the possible stardust values.
   *
   * @return A {@link List} with all possible stardust values.
   */
  public List<Integer> getStardustList() {
    List<Integer> sdList = new ArrayList<>(valuesBySD.keySet());
    Collections.sort(sdList);
    return sdList;
  }
}
