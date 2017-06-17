package net.ghielmetti.pokemon;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.utilities.Pair;

public class Pokemon implements Runnable {
  private final static Logger LOGGER = LoggerFactory.getLogger(Pokemon.class);
  private static Pokemon      instance;

  public Pokemon() {
    LOGGER.debug("Pokémons: {}", Pokedex.getInstance().toString());
  }

  public static Pokemon getInstance() {
    if (instance == null) {
      try {
        instance = new Pokemon();
      } catch (Exception e) {
        LOGGER.error("Unable to start Pokemon", e);
      }
    }
    return instance;
  }

  public static void main(final String[] inArguments) {
    if (inArguments.length == 0) {
      if (Pokemon.getInstance() != null) {
        new Thread(Pokemon.getInstance(), "Pokémon main").start();
      }
    } else {
      String approximatedName = inArguments[0];
      Integer cp = Integer.valueOf(inArguments[1]);
      Integer hp = Integer.valueOf(inArguments[2]);
      Integer stardust = Integer.valueOf(inArguments[3]);
      Limit limit = new Limit(inArguments[4]);

      for (String name : Pokedex.getNamesFromPartialName(approximatedName)) {
        Item item = Pokedex.getItem(name);
        List<Pair<Integer, IVLevel>> candidates = item.getCP(limit, cp.intValue(), hp.intValue(), stardust.intValue());
        if (!candidates.isEmpty()) {
          System.out.println();
          System.out.println(item);
          System.out.println();
          for (Pair<Integer, IVLevel> candidate : candidates) {
            System.out.println((candidate.getLeft().doubleValue() / 2.0) + " " + candidate.getRight());
          }
          System.out.println();
        }
      }
    }
  }

  @Override
  public void run() {
    LOGGER.debug("Start");
  }
}
