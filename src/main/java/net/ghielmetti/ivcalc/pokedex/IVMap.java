package net.ghielmetti.ivcalc.pokedex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.data.Limit;

/**
 * Map for all the possible IV combinations.<br>
 *
 * @author Leopoldo Ghielmetti
 */
public class IVMap {
  private static final Map<Limit, List<IVLevel>> map           = new HashMap<>();
  private static final List<Limit>               allowedLimits = new ArrayList<>();

  static {
    prepareAllPossibilities();
    associateLimitsWithIVLevels();
    removeImpossibleCases();
    Collections.sort(allowedLimits);
  }

  private IVMap() {
    // nothing to do
  }

  /**
   * Returns all the possible {@link IVLevel}s for the given {@link Limit}
   *
   * @param inLimit
   * @return A list with the IVLevels
   */
  public static List<IVLevel> getIVLevels(final Limit inLimit) {
    List<IVLevel> list = map.get(inLimit);
    return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
  }

  /**
   * Returns a list with all the possible (non empty and non generic) {@link Limit}s
   *
   * @return A list of {@link Limit}
   */
  public static List<Limit> getLimits() {
    return Collections.unmodifiableList(allowedLimits);
  }

  /**
   * Associate all the possibles {@link IVLevel}s to the corresponding {@link Limit}.
   */
  private static void associateLimitsWithIVLevels() {
    for (int a = 0; a <= 15; a++) {
      for (int d = 0; d <= 15; d++) {
        for (int s = 0; s <= 15; s++) {
          IVLevel iv = new IVLevel(a, d, s);

          for (Entry<Limit, List<IVLevel>> entry : map.entrySet()) {
            if (entry.getKey().matches(iv)) {
              entry.getValue().add(iv);
            }
          }
        }
      }
    }
  }

  /**
   * Prepare the lists for all possibilities.
   */
  private static void prepareAllPossibilities() {
    for (int level = -1; level <= 3; level++) {
      for (int strength = -1; strength <= 3; strength++) {
        for (char p = 0; p <= 7; p++) {
          Limit limit = new Limit(level, strength, (p & 4) != 0, (p & 2) != 0, (p & 1) != 0);

          if (level >= 0 && strength >= 0) {
            // We keep only the limits that makes sense on the user interface
            allowedLimits.add(limit);
          }

          // But we remember the cases for all the possible limits
          map.put(limit, new ArrayList<>());
        }
      }
    }
  }

  /**
   * The cases without {@link Limit}s must be removed because they don't select any valid Pokémon.
   */
  private static void removeImpossibleCases() {
    for (Entry<Limit, List<IVLevel>> e : new ArrayList<>(map.entrySet())) {
      if (e.getValue().isEmpty()) {
        map.remove(e.getKey());
        allowedLimits.remove(e.getKey());
      }
    }
  }
}
