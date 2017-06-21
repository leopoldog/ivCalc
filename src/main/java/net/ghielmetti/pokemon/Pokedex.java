package net.ghielmetti.pokemon;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

public class Pokedex {
  private final static Logger                    LOGGER     = LoggerFactory.getLogger(Pokedex.class);
  private final static Map<String, Item>         pokemons   = new HashMap<>();
  private final static Map<String, List<String>> evolutions = new HashMap<>();
  private static Pokedex                         instance;

  private Pokedex() {
    readBaseValues();
    readEvolutions();
  }

  public static Pokedex getInstance() {
    if (instance == null) {
      instance = new Pokedex();
    }
    return instance;
  }

  public Collection<String> getAllNames() {
    return pokemons.keySet();
  }

  public Item getItem(final String inName) {
    return pokemons.get(inName);
  }

  public List<String> getNamesFromPartialName(final String inPartialName) {
    String partialName = inPartialName.toLowerCase();
    return pokemons.keySet().stream() //
        .filter(name -> name.indexOf(partialName) != -1)//
        .collect(Collectors.toList());
  }

  private void readBaseValues() {
    try (InputStream is = getClass().getResourceAsStream("/baseValues.csv"); //
        InputStreamReader isr = new InputStreamReader(is); //
        CSVReader reader = new CSVReader(isr, '\t');) {
      String[] line;
      while ((line = reader.readNext()) != null) {
        String name = line[0];
        int attack = Integer.parseInt(line[1]);
        int defense = Integer.parseInt(line[2]);
        int stamina = Integer.parseInt(line[3]);
        pokemons.put(name.toLowerCase(), new Item(name, attack, defense, stamina));
      }
    } catch (Exception e) {
      LOGGER.error("Unable to read base values", e);
    }
  }

  private void readEvolutions() {
    try (InputStream is = getClass().getResourceAsStream("/evolutions.csv"); //
        InputStreamReader isr = new InputStreamReader(is); //
        CSVReader reader = new CSVReader(isr, '\t');) {
      String[] line;
      while ((line = reader.readNext()) != null) {
        String name = line[0];
        if (pokemons.containsKey(name.toLowerCase())) {
          String evolutionName = line[1];
          if (pokemons.containsKey(evolutionName.toLowerCase())) {
            List<String> list = evolutions.get(name.toLowerCase());
            if (list == null) {
              list = new ArrayList<>();
              evolutions.put(name.toLowerCase(), list);
            }
            list.add(evolutionName);
          } else {
            LOGGER.debug("Ignoring unknown evolution: {} -> {}", name, evolutionName);
          }
        } else {
          LOGGER.debug("Ignoring unknown Pokémon: {}", name);
        }
      }
    } catch (Exception e) {
      LOGGER.error("Unable to read evolutions", e);
    }
  }

  @Override
  public String toString() {
    return "Pokedex[pokemons=" + pokemons + ", evolutions=" + evolutions + "]";
  }
}
