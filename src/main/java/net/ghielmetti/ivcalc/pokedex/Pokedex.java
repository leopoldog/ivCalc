package net.ghielmetti.ivcalc.pokedex;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import net.ghielmetti.ivcalc.data.Pokemon;

/**
 * The Pokédex contains all the informations about Pokémon types.
 *
 * @author Leopoldo Ghielmetti
 */
public class Pokedex {
  private static final String            RESOURCE_NAMES       = "names";
  private static final String            RESOURCE_BASE_VALUES = "/baseValues.csv";
  private static final String            RESOURCE_EVOLUTIONS  = "/evolutions.csv";
  private static final String            RESOURCE_TYPES       = "/types.properties";
  private static final Logger            LOGGER               = LoggerFactory.getLogger(Pokedex.class);
  private final Map<String, Pokemon>     pokemons             = new HashMap<>();
  private final Map<String, Set<String>> evolutions           = new HashMap<>();
  private final List<String>             names                = new ArrayList<>();

  /** Constructor. */
  public Pokedex() {
    initialize();
  }

  /**
   * Returns all the known Pokémon names.
   *
   * @return The {@link List} of all Pokémon names.
   */
  public List<String> getAllNames() {
    return Collections.unmodifiableList(names);
  }

  /**
   * Returns all the known Pokémons.
   *
   * @return The {@link Collection} of all Pokémons.
   */
  public Collection<Pokemon> getAllPokemons() {
    return Collections.unmodifiableCollection(pokemons.values());
  }

  /**
   * Returns the list of all possible ancestors of a Pokémon.<br>
   * An ancestor is a Pokémon that evolves in the the specified one.
   *
   * @param inPokemon The Pokémon to get the ancestors.
   * @return The list of the ancestors in the reverse evolutionary order.
   */
  public List<Pokemon> getAncestors(final Pokemon inPokemon) {
    List<Pokemon> ancestors = new ArrayList<>();
    String name = inPokemon.getName().toLowerCase();

    for (Entry<String, Set<String>> entry : evolutions.entrySet()) {
      if (entry.getValue().contains(name)) {
        Pokemon pokemon = getPokemon(entry.getKey());
        ancestors.add(pokemon);
        ancestors.addAll(getAncestors(pokemon));
      }
    }

    return ancestors;
  }

  /**
   * Returns the list of all possible offsprings of a Pokémon.<br>
   * An offspring is an evolution of the Pokémon.
   *
   * @param inPokemon The Pokémon to get the offsprings.
   * @return The list of the offsprings in evolutionary order.
   */
  public List<Pokemon> getOffsprings(final Pokemon inPokemon) {
    List<Pokemon> offsprings = new ArrayList<>();
    Set<String> possibleEvolutions = evolutions.get(inPokemon.getName().toLowerCase());

    if (possibleEvolutions != null) {
      for (String name : possibleEvolutions) {
        Pokemon pokemon = getPokemon(name);
        offsprings.add(pokemon);
        offsprings.addAll(getOffsprings(pokemon));
      }
    }

    return offsprings;
  }

  /**
   * Returns the Pokémon with the given name.
   *
   * @param inName The Pokémon name.
   * @return The Pokémon.
   */
  public Pokemon getPokemon(final String inName) {
    return pokemons.get(inName.toLowerCase());
  }

  /**
   * Returns all the Pokémons matching the given name.
   *
   * @param inPartialName A partial name.
   * @return A {@link List} of all matching Pokémons.
   */
  public List<Pokemon> getPokemonsFromPartialName(final String inPartialName) {
    String partialName = inPartialName.toLowerCase();
    return pokemons.entrySet().stream() //
        .filter(entry -> entry.getKey().indexOf(partialName) != -1) //
        .map(Entry::getValue) //
        .collect(Collectors.toList());
  }

  /**
   * Initialize the Pokédex.<br>
   * This can be called when changed the {@link Locale} to get the correct Pokémon names translations.
   */
  public void initialize() {
    Properties types = readTypes();
    readBaseValues(types);
    readEvolutions();
  }

  @Override
  public String toString() {
    return "Pokedex[pokemons.size=" + pokemons.size() + ", evolutions.size=" + evolutions.size() + "]";
  }

  private Type[] getTypes(final Properties inTypes, final int id) {
    String[] types = inTypes.getProperty("n" + id).split(",");
    Type[] list = new Type[types.length];
    int i = 0;
    for (String t : types) {
      list[i++] = Type.valueOf(t);
    }
    return list;
  }

  private void instantiatePokemon(final ResourceBundle inBundle, final Properties inTypes, final String[] inCharacteristics) {
    int id = Integer.parseInt(inCharacteristics[0]);
    String name = inCharacteristics[1];
    int attack = Integer.parseInt(inCharacteristics[2]);
    int defense = Integer.parseInt(inCharacteristics[3]);
    int stamina = Integer.parseInt(inCharacteristics[4]);

    if (inBundle != null) {
      name = inBundle.getString("n" + id);
    }

    Pokemon pokemon = new Pokemon(id, name, attack, defense, stamina, getTypes(inTypes, id));
    pokemons.put(name.toLowerCase(), pokemon);
  }

  private void readBaseValues(final Properties inTypes) {
    ResourceBundle bundle = null;

    try {
      bundle = ResourceBundle.getBundle(RESOURCE_NAMES);
    } catch (Exception e) {
      LOGGER.info("Localized names for \"{}\" not found, using default", Locale.getDefault(), e);
    }

    try (InputStream is = getClass().getResourceAsStream(RESOURCE_BASE_VALUES); //
        InputStreamReader isr = new InputStreamReader(is); //
        CSVReader reader = new CSVReader(isr, '\t');) {
      String[] characteristics;

      while ((characteristics = reader.readNext()) != null) {
        instantiatePokemon(bundle, inTypes, characteristics);
      }

      names.addAll(pokemons.values().stream() //
          .map(Pokemon::getName) //
          .collect(Collectors.toList()));
      names.sort((a, b) -> a.compareTo(b));
    } catch (Exception e) {
      LOGGER.error("Unable to read base values", e);
    }
  }

  private void readEvolutions() {
    try (InputStream is = getClass().getResourceAsStream(RESOURCE_EVOLUTIONS); //
        InputStreamReader isr = new InputStreamReader(is); //
        CSVReader reader = new CSVReader(isr, '\t');) {
      String[] line;

      while ((line = reader.readNext()) != null) {
        String name = line[0].toLowerCase();

        if (pokemons.containsKey(name.toLowerCase())) {
          String evolutionName = line[1].toLowerCase();

          if (pokemons.containsKey(evolutionName.toLowerCase())) {
            Set<String> list = evolutions.get(name);

            if (list == null) {
              list = new HashSet<>();
              evolutions.put(name, list);
            }

            list.add(evolutionName);
          } else {
            LOGGER.trace("Ignoring unknown evolution: {} -> {}", name, evolutionName);
          }
        } else {
          LOGGER.trace("Ignoring unknown Pokémon: {}", name);
        }
      }
    } catch (Exception e) {
      LOGGER.error("Unable to read evolutions", e);
    }
  }

  private Properties readTypes() {
    try (InputStream is = getClass().getResourceAsStream(RESOURCE_TYPES)) {
      Properties prop = new Properties();
      prop.load(is);
      return prop;
    } catch (Exception e) {
      LOGGER.error("Unable to read the Types", e);
      return null;
    }
  }
}
