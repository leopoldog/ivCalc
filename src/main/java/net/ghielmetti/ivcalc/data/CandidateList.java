package net.ghielmetti.ivcalc.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

/**
 * Defines a list of Candidates.<br>
 * The half levels are considered by default.
 *
 * @author Leopoldo Ghielmetti
 */
public final class CandidateList {
  private static final String                          LF             = System.getProperty("line.separator");
  private final Pokemon                                pokemon;
  private final Limit                                  limit;
  private final SortedMap<Integer, SortedSet<IVLevel>> ivPerLevel     = new TreeMap<>();
  private int                                          sizeFull;
  private int                                          sizeHalf;
  private boolean                                      withHalfLevels = true;

  /**
   * Copy constructor for CandidateList object
   *
   * @param inCandidateList The object to copy.
   */
  public CandidateList(final CandidateList inCandidateList) {
    pokemon = inCandidateList.pokemon;
    limit = inCandidateList.limit;
    sizeFull = inCandidateList.sizeFull;
    sizeHalf = inCandidateList.sizeHalf;
    withHalfLevels = inCandidateList.withHalfLevels;

    for (Entry<Integer, SortedSet<IVLevel>> element : inCandidateList.ivPerLevel.entrySet()) {
      ivPerLevel.put(element.getKey(), new TreeSet<>(element.getValue()));
    }
  }

  /**
   * Creates a new CandidateList
   *
   * @param inPokemon The Pokemon.
   * @param inLimit The limit.
   */
  public CandidateList(final Pokemon inPokemon, final Limit inLimit) {
    pokemon = inPokemon;
    limit = inLimit;
  }

  /**
   * Creates a new CandidateList
   *
   * @param inPokemon The Pokemon.
   * @param inLimit The limits.
   * @param inList The list of IVLevels.
   */
  public CandidateList(final Pokemon inPokemon, final Limit inLimit, final List<ImmutablePair<Integer, IVLevel>> inList) {
    pokemon = inPokemon;
    limit = inLimit;

    for (ImmutablePair<Integer, IVLevel> element : inList) {
      addIVLevel(element.getLeft(), element.getRight());
    }
  }

  /**
   * Add the specified {@link IVLevel} to the candidates list for the level.
   *
   * @param inLevel The Pokémon level.
   * @param inIVLevel The {@link IVLevel}.
   */
  public void addIVLevel(final Integer inLevel, final IVLevel inIVLevel) {
    SortedSet<IVLevel> list = ivPerLevel.get(inLevel);

    if (list == null) {
      list = new TreeSet<>();
      ivPerLevel.put(inLevel, list);
    }

    list.add(inIVLevel);

    if ((inLevel.intValue() & 1) == 0) {
      sizeFull++;
    } else {
      sizeHalf++;
    }
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
    CandidateList other = (CandidateList) obj;
    if (limit == null) {
      if (other.limit != null) {
        return false;
      }
    } else if (!limit.equals(other.limit)) {
      return false;
    }
    if (pokemon == null) {
      if (other.pokemon != null) {
        return false;
      }
    } else if (!pokemon.equals(other.pokemon)) {
      return false;
    }
    return true;
  }

  /**
   * Filters the current candidates list using the given candidates list.
   *
   * @param inCandidateList The filter list.
   */
  public void filter(final CandidateList inCandidateList) {
    Set<IVLevel> ivLevels = new HashSet<>();

    inCandidateList.ivPerLevel.entrySet().stream().forEach(e -> {
      if (toTake(e.getKey())) {
        ivLevels.addAll(e.getValue());
      }
    });

    ivPerLevel.values().stream().forEach(v -> v.retainAll(ivLevels));
    ivPerLevel.entrySet().removeIf(e -> e.getValue().isEmpty());
  }

  /**
   * Returns a list of possible IVLevels for the specified level.
   *
   * @param inLevel The level.
   * @return A {@link List} of {@link IVLevel} or <code>null</code> if none.
   */
  public List<IVLevel> getIVLevels(final Integer inLevel) {
    List<IVLevel> list = new ArrayList<>();

    if (toTake(inLevel)) {
      list.addAll(ivPerLevel.get(inLevel));
    }

    return list;
  }

  /**
   * Returns the {@link Limit} used to define these candidates.
   *
   * @return The {@link Limit}.
   */
  public Limit getLimit() {
    return limit;
  }

  /**
   * Returns the possible maximum CP at level 40 for this list.
   *
   * @return The max CP.
   */
  public int getMaxCP() {
    int max = 0;

    for (Entry<Integer, SortedSet<IVLevel>> entry : ivPerLevel.entrySet()) {
      if (toTake(entry.getKey())) {
        for (IVLevel ivLevel : entry.getValue()) {
          max = Math.max(max, pokemon.getCPForLevel(80, ivLevel));
        }
      }
    }

    return max;
  }

  /**
   * Returns the Pokémon.
   *
   * @return The Pokémon.
   */
  public Pokemon getPokemon() {
    return pokemon;
  }

  /**
   * Returns a {@link List} of possible levels.
   *
   * @return The possible levels.
   */
  public List<Integer> getPossibleLevels() {
    return ivPerLevel.keySet().stream().filter(this::toTake).collect(Collectors.toList());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (limit == null ? 0 : limit.hashCode());
    result = prime * result + (pokemon == null ? 0 : pokemon.hashCode());
    return result;
  }

  /**
   * Accessor.
   *
   * @return <code>true</code> if the list also returns half levels.
   */
  public boolean isWithHalfLevels() {
    return withHalfLevels;
  }

  /**
   * Change the behavior of the list. If the half levels are enabled, the list will return also the half levels.<br>
   * If disabled, the list acts as the half levels where not present.<br>
   * Is possible to change this argument as will, the half levels will appear and disappear from the list as requested.
   *
   * @param inWithHalfLevels <code>true</code> if the half levels are considered.
   */
  public void setWithHalfLevels(final boolean inWithHalfLevels) {
    withHalfLevels = inWithHalfLevels;
  }

  /**
   * Returns the number of candidates in this list.
   *
   * @return The size.
   */
  public int size() {
    if (withHalfLevels) {
      return sizeFull + sizeHalf;
    }

    return sizeFull;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    ImmutableTriple<Integer, Integer, Integer> baseValues = pokemon.getBaseValues();

    sb.append(LF).append(pokemon.getName()).append(":").append(LF) //
        .append("  Base attack : ").append(baseValues.getLeft()).append(LF) //
        .append("  Base defense: ").append(baseValues.getMiddle()).append(LF) //
        .append("  Base stamina: ").append(baseValues.getRight()).append(LF) //
        .append(LF);

    for (Entry<Integer, SortedSet<IVLevel>> entry : ivPerLevel.entrySet()) {
      if (toTake(entry.getKey())) {
        String trueLevel = Double.toString(entry.getKey().doubleValue() / 2.0);

        for (IVLevel ivLevel : entry.getValue()) {
          sb.append(trueLevel).append(" ").append(ivLevel).append(LF);
        }
      }
    }

    return sb.toString();
  }

  private boolean toTake(final Integer inLevel) {
    return withHalfLevels || (inLevel.intValue() & 1) == 0;
  }
}
