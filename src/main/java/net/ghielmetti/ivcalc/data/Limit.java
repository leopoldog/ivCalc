package net.ghielmetti.ivcalc.data;

/**
 * A {@link Limit} represent a mask for the Pokémon levels.<br>
 * Once defined, a limit allow to check if a particular {@link IVLevel} matches the filter. This correspond to the
 * sentences pronounced by the Team Leader when asked to appraise the Pokémon.<br>
 * <br>
 * The possible levels are:<br>
 * <li>-1 not specified (matches all)</li>
 * <li>0 sum of IV between 0% and 51%</li>
 * <li>1 sum of IV between 51% and 66%</li>
 * <li>2 sum of IV between 66% and 82%</li>
 * <li>3 sum of IV between 82% and 100%</li><br>
 * The possible strength are:<br>
 * <li>-1 not specified (matches all)</li>
 * <li>0 max IV values between 0 and 7</li>
 * <li>1 max IV values between 8 and 12</li>
 * <li>2 max IV values between 13 and 14</li>
 * <li>3 max IV values equals to 15</li> <br>
 * A code can also be associated to a Limit, in this case the code is made up to specify the Limit parameters.
 *
 * @author Leopoldo Ghielmetti
 */
public final class Limit implements Comparable<Limit> {
  private int     level;
  private int     strength;
  private boolean a;
  private boolean h;
  private boolean d;
  private int     min;
  private int     max;
  private int     minStrength;
  private int     maxStrength;

  /**
   * Creates a new {@link Limit} object.
   *
   * @param inLevel The wanted level
   * @param inStrength The wanted strength
   * @param inHP If the HP are the highest
   * @param inAttack If the Attack are the highest
   * @param inDefense If the Defense are the highest
   */
  public Limit(final int inLevel, final int inStrength, final boolean inHP, final boolean inAttack, final boolean inDefense) {
    initialize(inLevel, inStrength, inHP, inAttack, inDefense);
  }

  /**
   * Creates a new {@link Limit} object from the specified {@link IVLevel}.
   *
   * @param inIVLevel The source {@link IVLevel}.
   */
  public Limit(final IVLevel inIVLevel) {
    if (inIVLevel.getTotal() < 23) {
      level = 0;
    } else if (inIVLevel.getTotal() < 30) {
      level = 1;
    } else if (inIVLevel.getTotal() < 37) {
      level = 2;
    } else {
      level = 3;
    }

    if (inIVLevel.getMaxIV() < 8) {
      strength = 0;
    } else if (inIVLevel.getMaxIV() < 13) {
      strength = 1;
    } else if (inIVLevel.getMaxIV() < 15) {
      strength = 2;
    } else {
      strength = 3;
    }

    initialize(level, strength, inIVLevel.isStamina(), inIVLevel.isAttack(), inIVLevel.isDefense());
  }

  /**
   * Creates a new {@link Limit} object.
   *
   * @param inDescription The description of the limit
   */
  public Limit(final String inDescription) {
    String description = inDescription.isEmpty() ? "xx-had" : inDescription.toLowerCase();
    char l = description.charAt(0);
    char s = description.length() > 1 ? description.charAt(1) : 'x';

    level = l == 'x' ? -1 : l == 0 ? 1 : l - '1';
    strength = s == 'x' ? -1 : 'd' - s;
    String detail = description.length() > 2 ? description.substring(2) : "";
    initialize(level, strength, detail.indexOf('h') != -1, detail.indexOf('a') != -1, detail.indexOf('d') != -1);
  }

  @Override
  public int compareTo(final Limit inOther) {
    if (level < inOther.level) {
      return 1;
    }
    if (level > inOther.level) {
      return -1;
    }
    if (strength < inOther.strength) {
      return 1;
    }
    if (strength > inOther.strength) {
      return -1;
    }
    if (!h && inOther.h) {
      return 1;
    }
    if (h && !inOther.h) {
      return -1;
    }
    if (!a && inOther.a) {
      return 1;
    }
    if (a && !inOther.a) {
      return -1;
    }
    if (!d && inOther.d) {
      return 1;
    }
    if (d && !inOther.d) {
      return -1;
    }
    return 0;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Limit other = (Limit) obj;
    return a == other.a && d == other.d && h == other.h && level == other.level && strength == other.strength;
  }

  /**
   * Returns the level specifier for this limit.
   *
   * @return The level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Returns the strength specifier for this limit.
   *
   * @return The strength
   */
  public int getStrength() {
    return strength;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (a ? 1231 : 1237);
    result = prime * result + (d ? 1231 : 1237);
    result = prime * result + (h ? 1231 : 1237);
    result = prime * result + level;
    result = prime * result + strength;
    return result;
  }

  /**
   * Returns <code>true</code> if the attack is the strongest IV.
   *
   * @return <code>true</code> if it is the strongest
   */
  public boolean isAttack() {
    return a;
  }

  /**
   * Returns <code>true</code> if the defense is the strongest IV.
   *
   * @return <code>true</code> if it is the strongest
   */
  public boolean isDefense() {
    return d;
  }

  /**
   * Returns <code>true</code> if the hp is the strongest IV.
   *
   * @return <code>true</code> if it is the strongest
   */
  public boolean isHP() {
    return h;
  }

  /**
   * Returns <code>true</code> if this limit has valid values.
   *
   * @return <code>true</code> if valid
   */
  public boolean isValid() {
    return a || d || h;
  }

  /**
   * Returns true if the specified IV matches this Limit.
   *
   * @param inIV The IV to match
   * @return <code>true</code> if matches.
   */
  public boolean matches(final IVLevel inIV) {
    int sum = inIV.getTotal();
    int maxIV = inIV.getMaxIV();
    boolean ta = a == inIV.isAttack();
    boolean td = d == inIV.isDefense();
    boolean ts = h == inIV.isStamina();
    boolean testSum = sum >= min && sum <= max;
    boolean testStrength = maxIV >= minStrength && maxIV <= maxStrength;
    boolean testIV = ta && td && ts;
    return testSum && testStrength && testIV;
  }

  @Override
  public String toString() {
    return Character.toString("x1234".charAt(level + 1)) + "xdcba".charAt(strength + 1) + "-" + (h ? "h" : "") + (a ? "a" : "") + (d ? "d" : "");
  }

  /**
   * Initializes this limit.
   *
   * @param inLevel The level (between -1 and 3)
   * @param inStrength The strength (between -1 and 3)
   * @param inHP <code>true</code> if the hp is the strongest
   * @param inAttack <code>true</code> if the attack is the strongest
   * @param inDefense <code>true</code> if the defense is the strongest
   */
  private void initialize(final int inLevel, final int inStrength, final boolean inHP, final boolean inAttack, final boolean inDefense) {
    level = inLevel;
    strength = inStrength;
    h = inHP;
    a = inAttack;
    d = inDefense;

    switch (inLevel) {
      case 3:
        max = 45;
        min = 37;
        break;
      case 2:
        max = 36;
        min = 30;
        break;
      case 1:
        max = 29;
        min = 23;
        break;
      case 0:
        max = 22;
        min = 0;
        break;
      default:
        level = -1;
        max = 45;
        min = 0;
        break;
    }

    switch (inStrength) {
      case 3:
        maxStrength = 15;
        minStrength = 15;
        break;
      case 2:
        maxStrength = 14;
        minStrength = 13;
        break;
      case 1:
        maxStrength = 12;
        minStrength = 8;
        break;
      case 0:
        maxStrength = 7;
        minStrength = 0;
        break;
      default:
        strength = -1;
        maxStrength = 15;
        minStrength = 0;
        break;
    }
  }
}
