package net.ghielmetti.pokemon;

import java.util.List;

import net.ghielmetti.utilities.Pair;

public class Pokemon {
  public static void main(final String[] inArguments) {
    if (inArguments.length == 0) {
      PokemonController controller = new PokemonController();
      new Thread(controller, "Pokémon").start();
    } else {
      String approximatedName = inArguments[0];
      Integer cp = Integer.valueOf(inArguments[1]);
      Integer hp = Integer.valueOf(inArguments[2]);
      Integer stardust = Integer.valueOf(inArguments[3]);
      Limit limit = new Limit(inArguments[4]);

      for (String name : Pokedex.getInstance().getNamesFromPartialName(approximatedName)) {
        Item item = Pokedex.getInstance().getItem(name);
        List<Pair<Integer, IVLevel>> candidates = item.getIV(limit, cp.intValue(), hp.intValue(), stardust.intValue());
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
}
