package net.ghielmetti.pokemon;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import net.ghielmetti.utilities.Pair;

public class Multiplier {
  private final static Logger                       LOGGER        = LoggerFactory.getLogger(Multiplier.class);
  private static Multiplier                         instance;
  private Map<Integer, List<Pair<Integer, Double>>> valuesBySD    = new HashMap<>();
  private Map<Integer, Double>                      valuesByLevel = new HashMap<>();

  private Multiplier() {
    try (InputStream is = getClass().getResourceAsStream("/multipliers.csv"); //
        InputStreamReader isr = new InputStreamReader(is); //
        CSVReader reader = new CSVReader(isr, '\t');) {
      String[] line;
      while ((line = reader.readNext()) != null) {
        Integer stardust = Integer.valueOf(line[2]);
        List<Pair<Integer, Double>> list = valuesBySD.get(stardust);
        if (list == null) {
          list = new ArrayList<>();
          valuesBySD.put(stardust, list);
        }
        Integer level = Integer.valueOf((int) (Float.parseFloat(line[0]) * 2.0));
        Double multiplier = Double.valueOf(line[1]);
        valuesByLevel.put(level, multiplier);
        list.add(new Pair<>(level, multiplier));
      }
    } catch (Exception e) {
      LOGGER.error("Unable to instantiate the Multiplier", e);
    }
  }

  public static Multiplier getInstance() {
    if (instance == null) {
      instance = new Multiplier();
    }
    return instance;
  }

  public Double getMultiplier(final int inLevel) {
    return valuesByLevel.get(Integer.valueOf(inLevel));
  }

  public List<Pair<Integer, Double>> getMultipliers(final int inStardust) {
    List<Pair<Integer, Double>> list = valuesBySD.get(Integer.valueOf(inStardust));
    return Collections.unmodifiableList(list == null ? new ArrayList<>() : list);
  }

  public List<Integer> getStardustList() {
    List<Integer> sdList = new ArrayList<>(valuesBySD.keySet());
    Collections.sort(sdList);
    return sdList;
  }
}
