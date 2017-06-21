package net.ghielmetti.pokemon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ghielmetti.utilities.Pair;

public class Item {
  private String name;
  private int    attack;
  private int    defense;
  private int    stamina;

  public Item(final String inName, final int inAttack, final int inDefense, final int inStamina) {
    name = inName;
    attack = inAttack;
    defense = inDefense;
    stamina = inStamina;
  }

  public int getBaseAttack() {
    return attack;
  }

  public int getBaseDefense() {
    return defense;
  }

  public int getBaseStamina() {
    return stamina;
  }

  public Map<Integer, Integer> getCP(final int inStardust, final int inIVAttack, final int inIVDefense, final int inIVStamina) {
    Map<Integer, Integer> cpList = new HashMap<>();
    List<Pair<Integer, Double>> candidates = Multiplier.getInstance().getMultipliers(inStardust);
    for (Pair<Integer, Double> candidate : candidates) {
      double a = (attack + inIVAttack) * candidate.getRight().doubleValue();
      double d = (defense + inIVDefense) * candidate.getRight().doubleValue();
      double s = (stamina + inIVStamina) * candidate.getRight().doubleValue();
      cpList.put(candidate.getLeft(), Integer.valueOf(Math.max(10, (int) ((Math.sqrt(s * d) * a) / 10.0))));
    }
    return cpList;
  }

  public List<Pair<Integer, IVLevel>> getCP(final Limit inLimit, final int inCP, final int inHP, final int inStardust) {
    List<Pair<Integer, IVLevel>> cpList = new ArrayList<>();
    List<Pair<Integer, Double>> candidates = Multiplier.getInstance().getMultipliers(inStardust);
    Collection<IVLevel> levels = IVMap.getIVLevels(inLimit);

    if (!levels.isEmpty()) {
      for (Pair<Integer, Double> candidate : candidates) {
        for (IVLevel level : levels) {
          double s = (stamina + level.getStamina()) * candidate.getRight().doubleValue();
          if ((int) Math.floor(s) == inHP) {
            double a = (attack + level.getAttack()) * candidate.getRight().doubleValue();
            double d = (defense + level.getDefense()) * candidate.getRight().doubleValue();
            int cp = (int) Math.floor(Math.max(10.0, (Math.sqrt(s * d) * a) / 10.0));

            if (cp == inCP) {
              cpList.add(new Pair<>(candidate.getLeft(), level));
            }
          }
        }
      }
    }
    return cpList;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return String.format("%s:%n  Base attack : %3d%n  Base defense: %3d%n  Base stamina: %3d", name, Integer.valueOf(attack), Integer.valueOf(defense), Integer.valueOf(stamina));
  }
}