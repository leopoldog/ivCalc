package net.ghielmetti.pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IVMap {
  private static final String                    DEFENSE_CODE   = "d";
  private static final String                    ATTACK_CODE    = "a";
  private static final String                    HP_CODE        = "h";
  private static final String                    STRENGTH_CODES = "xabcd";
  private static final String                    LEVEL_CODES    = "x01234";
  private static final Map<Limit, List<IVLevel>> map            = new HashMap<>();
  private static final List<Limit>               allowedLimits  = new ArrayList<>();

  static {
    for (int il = LEVEL_CODES.length() - 1; il >= 0; il--) {
      for (int is = STRENGTH_CODES.length() - 1; is >= 0; is--) {
        for (char p = 0; p <= 7; p++) {
          char l = LEVEL_CODES.charAt(il);
          char s = STRENGTH_CODES.charAt(is);
          String limitName = Character.toString(l) + s + "-" + ((p & 4) == 0 ? "" : HP_CODE) + ((p & 2) == 0 ? "" : ATTACK_CODE) + ((p & 1) == 0 ? "" : DEFENSE_CODE);
          Limit limit = new Limit(limitName);
          if ((il >= 2) && (is >= 1)) {
            allowedLimits.add(limit);
          }
          map.put(limit, new ArrayList<>());
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

    for (Entry<Limit, List<IVLevel>> e : new ArrayList<>(map.entrySet())) {
      if (e.getValue().isEmpty()) {
        map.remove(e.getKey());
        allowedLimits.remove(e.getKey());
      }
    }

    Collections.sort(allowedLimits);
  }

  private IVMap() {
    // nothing to do
  }

  public static List<IVLevel> getIVLevels(final Limit inLimit) {
    List<IVLevel> list = map.get(inLimit);
    return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
  }

  public static List<Limit> getLimits() {
    return Collections.unmodifiableList(allowedLimits);
  }
}
