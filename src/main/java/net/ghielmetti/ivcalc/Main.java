package net.ghielmetti.ivcalc;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.gui.PokemonController;
import net.ghielmetti.ivcalc.gui.model.PokemonModel;
import net.ghielmetti.ivcalc.pokedex.Pokedex;
import net.ghielmetti.ivcalc.tools.PreferenceStore;

/**
 * Main Pokémon application.
 *
 * @author Leopoldo Ghielmetti
 */
public class Main {
  private static final Logger OUTPUT = LoggerFactory.getLogger("Output");

  private Main() {
    // nothing to do
  }

  /**
   * If the main is called without arguments, starts the GUI.<br>
   * If the arguments are given, compute the Pokémon without opening the GUI.
   *
   * @param inArguments The arguments.
   */
  public static void main(final String[] inArguments) {
    new Main().mainApplication(inArguments);
  }

  /**
   * Executes the main Pokémon's application.
   *
   * @param inArguments The application arguments (if any).
   */
  void mainApplication(final String[] inArguments) {
    Locale.setDefault(PreferenceStore.getLocale());

    if (inArguments.length == 0) {
      startPokemonGUI();
    } else if (inArguments.length == 5) {
      searchPokemons(inArguments);
    } else {
      showHelp();
    }
  }

  /**
   * Searches for matching Pokémons.
   *
   * @param inArguments The program arguments.
   */
  void searchPokemons(final String[] inArguments) {
    PokemonModel model = new PokemonModel(null, null, null);
    model.setPokemons(new Pokedex().getPokemonsFromPartialName(inArguments[0]));
    model.setCP(Integer.valueOf(inArguments[1]));
    model.setHP(Integer.valueOf(inArguments[2]));
    model.setSD(Integer.valueOf(inArguments[3]));
    model.setLimit(new Limit(inArguments[4]));
    model.searchCandidates();
  }

  /** Displays the help information on the console. */
  void showHelp() {
    OUTPUT.error("Wrong number of arguments, you should specify 5 arguments:\n" //
        + "  1. The Pokémon name (or a partial name)\n" //
        + "  2. The CP\n" //
        + "  3. The HP\n" //
        + "  3. The SD\n" //
        + "  5. The code representing the Pokémon appreciation.");
  }

  /** Starts the Pokémon GUI. */
  void startPokemonGUI() {
    new PokemonController().start();
  }
}
