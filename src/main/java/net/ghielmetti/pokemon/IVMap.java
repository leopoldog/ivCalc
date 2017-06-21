package net.ghielmetti.pokemon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IVMap {
  private static final Map<Limit, List<IVLevel>> map = new HashMap<>();

  static {
    for (char l = '1'; l <= '4'; l++) {
      for (char s = 'a'; s <= 'd'; s++) {
        for (char p = 1; p <= 7; p++) {
          map.put(new Limit(Character.toString(l) + s + "-" + ((p & 4) == 0 ? "h" : "") + ((p & 2) == 0 ? "a" : "") + ((p & 1) == 0 ? "d" : "")), new ArrayList<>());
        }
      }
    }

    for (int a = 0; a <= 15; a++) {
      for (int d = 0; d <= 15; d++) {
        for (int s = 0; s <= 15; s++) {
          IVLevel iv = new IVLevel(a, d, s);

          for (Entry<Limit, List<IVLevel>> entry : map.entrySet()) {
            if (entry.getKey().matches(a, d, s)) {
              entry.getValue().add(iv);
            }
          }
        }
      }
    }
  }

  private IVMap() {
    // nothing to do
  }

  public static List<IVLevel> getIVLevels(final Limit inLimit) {
    return Collections.unmodifiableList(map.get(inLimit));
  }

  public static Collection<Limit> getLimits() {
    return map.keySet();
  }
}
