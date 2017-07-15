package net.ghielmetti.pokemon;

import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.pokemon.PokemonView.ComputePokemonListener;
import net.ghielmetti.pokemon.team.Team;
import net.ghielmetti.pokemon.team.TeamDialog;
import net.ghielmetti.utilities.Pair;

public class PokemonController implements Runnable {
  private final static Logger    LOGGER                 = LoggerFactory.getLogger(PokemonController.class);
  private static Team            team;
  private PokemonView            view;
  private PokemonModel           model;
  private ComputePokemonListener computePokemonListener = this::computePokemon;

  public PokemonController() {
    LOGGER.debug("Pokémons: {}", Pokedex.getInstance().toString());
  }

  @Override
  public void run() {
    try {
      if (getTeam() != null) {
        model = new PokemonModel();
        view = new PokemonView(computePokemonListener, team);
        view.setVisible(true);
      }
    } catch (Exception e) {
      LOGGER.error("Error in execution", e);
    }
  }

  private void computePokemon(final List<String> inNames) {
    Integer cp = view.getCP();
    Integer hp = view.getHP();
    Integer stardust = view.getSD();
    Limit limit = view.getCode();
    view.clear();

    new Thread(() -> {
      int total = 0;

      for (String name : inNames) {
        Item item = Pokedex.getInstance().getItem(name);
        List<Pair<Integer, IVLevel>> candidates = item.getIV(limit, cp.intValue(), hp.intValue(), stardust.intValue());

        if (!candidates.isEmpty()) {
          sortCandidates(candidates);
          total += candidates.size();
          view.outputCandidate(item, cp, candidates);
          view.setFound(total);
          if (total > 5000) {
            view.outputCandidate(null, null, null);
            JOptionPane.showMessageDialog(view, "More than 5000 Pokémons found, displaying only the first " + total, "Too many Pokémons", JOptionPane.OK_OPTION);
            break;
          }
        }
      }
    }, "Search Pokémons").start();
  }

  private Team getTeam() {
    Preferences prefs = Preferences.userRoot().node("pokemon");
    try {
      team = Team.valueOf(prefs.get("team.name", null));
    } catch (Exception e) {
      LOGGER.debug("Invalid team name, asking for the team");
      TeamDialog dialog = new TeamDialog(null);

      dialog.setVisible(true);

      if (dialog.getTeam() == null) {
        return null;
      }
      team = dialog.getTeam();
      prefs.put("team.name", team.name());
    }
    return team;
  }

  private void sortCandidates(final List<Pair<Integer, IVLevel>> inCandidates) {
    Collections.sort(inCandidates, (p1, p2) -> {
      int c = p1.getLeft().compareTo(p2.getLeft());
      if (c == 0) {
        c = p1.getRight().compareTo(p2.getRight());
      }
      return c;
    });
  }
}
