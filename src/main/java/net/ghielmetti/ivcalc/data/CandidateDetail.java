package net.ghielmetti.ivcalc.data;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * This objects represents a detail for a candidate.
 *
 * @author Leopoldo Ghielmetti
 */
public class CandidateDetail {
  private boolean                         bad;
  private ImmutablePair<Integer, Integer> minMaxCP;
  private Integer                         level;
  private IVLevel                         ivLevel;
  private Integer                         cp;

  /**
   * Constructor.
   *
   * @param inIsBad <code>true</code> if the detail refers to a "bad" Pokémon candidate.
   * @param inMinMaxCP the minimum/maximum possible values for the CP.
   * @param inLevel the level of this detail.
   * @param inIvLevel the IV level of this detail.
   * @param inCP the current CP.
   */
  public CandidateDetail(final boolean inIsBad, final ImmutablePair<Integer, Integer> inMinMaxCP, final Integer inLevel, final IVLevel inIvLevel, final Integer inCP) {
    bad = inIsBad;
    minMaxCP = inMinMaxCP;
    level = inLevel;
    ivLevel = inIvLevel;
    cp = inCP;
  }

  /**
   * Accessor.
   *
   * @return The CP value.
   */
  public Integer getCP() {
    return cp;
  }

  /**
   * Accessor.
   *
   * @return The IV level of this detail.
   */
  public IVLevel getIVLevel() {
    return ivLevel;
  }

  /**
   * Accessor.
   *
   * @return The level of this Pokémon detail.
   */
  public Integer getLevel() {
    return level;
  }

  /**
   * Accessor.
   *
   * @return The minimum/maximum CP value.
   */
  public ImmutablePair<Integer, Integer> getMinMaxCP() {
    return minMaxCP;
  }

  /**
   * Accessor.
   *
   * @return <code>true</code> if this detail is "bad" (displayed in RED).
   */
  public boolean isBad() {
    return bad;
  }

  @Override
  public String toString() {
    return "CandidateDetail[bad=" + bad + ", level=" + level + ", ivLevel=" + ivLevel + ", cp=" + cp + ", minMaxCP=" + minMaxCP + "]";
  }
}
