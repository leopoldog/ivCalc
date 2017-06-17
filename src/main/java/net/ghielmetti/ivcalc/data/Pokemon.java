package net.ghielmetti.ivcalc.data;

import java.awt.Image;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import net.ghielmetti.ivcalc.pokedex.IVMap;
import net.ghielmetti.ivcalc.pokedex.Multiplier;
import net.ghielmetti.ivcalc.pokedex.Type;

/**
 * Represent a Pokémon with his characteristics.
 *
 * @author Leopoldo Ghielmetti
 */
public final class Pokemon implements Comparable<Pokemon> {
  private final ImageIcon icon;
  private final ImageIcon smallIcon;
  private final int       number;
  private final String    name;
  private final int       baseAttack;
  private final int       baseDefense;
  private final int       baseStamina;
  private final Type[]    types;

  /**
   * Creates a new Pokémon.
   *
   * @param inNumber The Pokémon number in the Pokédex.
   * @param inName The Pokémon name.
   * @param inBaseAttack The base attack.
   * @param inBaseDefense The base defense.
   * @param inBaseStamina The base stamina.
   * @param inTypes The Pokémon types (1 or 2).
   */
  public Pokemon(final int inNumber, final String inName, final int inBaseAttack, final int inBaseDefense, final int inBaseStamina, final Type... inTypes) {
    number = inNumber;
    name = inName;
    baseAttack = inBaseAttack;
    baseDefense = inBaseDefense;
    baseStamina = inBaseStamina;
    types = inTypes;

    URL iconURL = getClass().getResource("/images/" + inNumber + ".png");

    if (iconURL == null) {
      icon = null;
      smallIcon = null;
    } else {
      icon = new ImageIcon(iconURL);
      smallIcon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
    }
  }

  /**
   * Compute the CP for a pokemon with the specified points.
   *
   * @param inAttack The attack points.
   * @param inDefense The defense points.
   * @param inStamina The stamina points.
   * @return The CP.
   */
  private static int computeCP(final double inAttack, final double inDefense, final double inStamina) {
    return Math.max((int) Math.floor(Math.sqrt(inStamina * inDefense) * inAttack / 10.0), 10);
  }

  @Override
  public int compareTo(final Pokemon inOther) {
    return name.compareTo(inOther.name);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Pokemon other = (Pokemon) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  /**
   * Returns the base points for this Pokémon.
   *
   * @return The base points.
   */
  public ImmutableTriple<Integer, Integer, Integer> getBaseValues() {
    return ImmutableTriple.of(Integer.valueOf(baseAttack), Integer.valueOf(baseDefense), Integer.valueOf(baseStamina));
  }

  /**
   * Returns the CP for the given level and {@link IVLevel}.
   *
   * @param inLevel The level.
   * @param inIVLevel The IVLevel.
   * @return The CP.
   */
  public int getCPForLevel(final int inLevel, final IVLevel inIVLevel) {
    double multiplier = Multiplier.getInstance().getMultiplier(inLevel).doubleValue();
    double a = (baseAttack + inIVLevel.getAttack()) * multiplier;
    double d = (baseDefense + inIVLevel.getDefense()) * multiplier;
    double s = (baseStamina + inIVLevel.getStamina()) * multiplier;
    return computeCP(a, d, s);
  }

  /**
   * Returns all the possible CP for the Pokémon with the specified {@link IVLevel} and stardust.
   *
   * @param inStardust The stardust.
   * @param inIV The IV.
   * @return A list of all possible CP.
   */
  public Map<Integer, Integer> getCPForStardust(final int inStardust, final IVLevel inIV) {
    Map<Integer, Integer> cpList = new HashMap<>();
    List<ImmutablePair<Integer, Double>> candidates = Multiplier.getInstance().getMultipliers(inStardust);
    for (ImmutablePair<Integer, Double> candidate : candidates) {
      double a = (baseAttack + inIV.getAttack()) * candidate.getRight().doubleValue();
      double d = (baseDefense + inIV.getDefense()) * candidate.getRight().doubleValue();
      double s = (baseStamina + inIV.getStamina()) * candidate.getRight().doubleValue();
      cpList.put(candidate.getLeft(), Integer.valueOf(computeCP(a, d, s)));
    }
    return cpList;
  }

  /**
   * Returns the icon of this Pokémon (if present).
   *
   * @return The icon or <code>null</code>.
   */
  public Icon getIcon() {
    return icon;
  }

  /**
   * Returns a list of possible IV for this Pokémon given the game characteristics.
   *
   * @param inLimit The {@link Limit} that constraints the possible {@link IVLevel}s.
   * @param inCP The given CP.
   * @param inHP The given HP.
   * @param inStardust The given stardust.
   * @return A {@link CandidateList}.
   */
  public CandidateList getIV(final Limit inLimit, final int inCP, final int inHP, final int inStardust) {
    List<ImmutablePair<Integer, Double>> multipliers = Multiplier.getInstance().getMultipliers(inStardust);
    Collection<IVLevel> levels = IVMap.getIVLevels(inLimit);
    CandidateList candidates = new CandidateList(this, inLimit);

    if (!levels.isEmpty()) {
      for (ImmutablePair<Integer, Double> multiplier : multipliers) {
        for (IVLevel level : levels) {
          double s = (baseStamina + level.getStamina()) * multiplier.getRight().doubleValue();

          if (Math.max((int) Math.floor(s), 10) == inHP) {
            double a = (baseAttack + level.getAttack()) * multiplier.getRight().doubleValue();
            double d = (baseDefense + level.getDefense()) * multiplier.getRight().doubleValue();
            int cp = computeCP(a, d, s);

            if (cp == inCP) {
              candidates.addIVLevel(multiplier.getLeft(), level);
            }
          }
        }
      }
    }

    return candidates;
  }

  /**
   * Returns the possible min and max CP for this Pokémon at a specified level.
   *
   * @param inLevel The level we are interested for.
   * @return An {@link ImmutablePair} with the minimum and the maximum CP values.
   */
  public ImmutablePair<Integer, Integer> getMinMaxCP(final int inLevel) {
    double multiplier = Multiplier.getInstance().getMultiplier(inLevel).doubleValue();
    double aMax = (baseAttack + 15) * multiplier;
    double dMax = (baseDefense + 15) * multiplier;
    double sMax = (baseStamina + 15) * multiplier;
    double aMin = baseAttack * multiplier;
    double dMin = baseDefense * multiplier;
    double sMin = baseStamina * multiplier;
    return ImmutablePair.of(Integer.valueOf(computeCP(aMin, dMin, sMin)), Integer.valueOf(computeCP(aMax, dMax, sMax)));
  }

  /**
   * Returns the name of the Pokémon.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the number of this Pokémon.
   *
   * @return The number.
   */
  public int getNumber() {
    return number;
  }

  /**
   * Returns a small version of the icon of this Pokémon (if present).
   *
   * @return The icon or <code>null</code>.
   */
  public Icon getSmallIcon() {
    return smallIcon;
  }

  /**
   * The Pokémon type.
   *
   * @return The {@link Type} array.
   */
  public Type[] getTypes() {
    return types;
  }

  /**
   * The Pokémon values for the given level and {@link IVLevel}.
   *
   * @param inLevel The level.
   * @param inIVLevel The {@link IVLevel}.
   * @return An {@link ImmutableTriple} of values in the order (Attack, Defense, Stamina).
   */
  public ImmutableTriple<Integer, Integer, Integer> getValues(final int inLevel, final IVLevel inIVLevel) {
    double multiplier = Multiplier.getInstance().getMultiplier(inLevel).doubleValue();
    double a = (baseAttack + inIVLevel.getAttack()) * multiplier;
    double d = (baseDefense + inIVLevel.getDefense()) * multiplier;
    double s = (baseStamina + inIVLevel.getStamina()) * multiplier;
    return ImmutableTriple.of(Integer.valueOf((int) a), Integer.valueOf((int) d), Integer.valueOf((int) s));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return name;
  }
}
