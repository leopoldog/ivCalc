package net.ghielmetti.pokemon;

public class Limit implements Comparable<Limit> {
  private int     level;
  private int     strength;
  private boolean a;
  private boolean h;
  private boolean d;
  private int     min;
  private int     max;
  private int     minStrength;
  private int     maxStrength;

  public Limit(final int inLevel, final int inStrength, final boolean inHP, final boolean inAttack, final boolean inDefense) {
    initialize(inLevel, inStrength, inHP, inAttack, inDefense);
  }

  public Limit(final String inDescription) {
    String description = inDescription.toLowerCase();
    level = description.charAt(0) - '0';
    strength = 'e' - description.charAt(1);
    String detail = description.substring(2);
    initialize(level, strength, detail.indexOf('h') != -1, detail.indexOf('a') != -1, detail.indexOf('d') != -1);
  }

  public boolean canBe(final int inIVA, final int inIVS) {
    int sum = inIVA + inIVS;
    return ((sum + 15) >= min) && (sum <= max);
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
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    Limit other = (Limit) obj;
    return (a == other.a) && (d == other.d) && (h == other.h) && (level == other.level) && (strength == other.strength);
  }

  public int getLevel() {
    return level;
  }

  public int getStrength() {
    return strength;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + (a ? 1231 : 1237);
    result = (prime * result) + (d ? 1231 : 1237);
    result = (prime * result) + (h ? 1231 : 1237);
    result = (prime * result) + level;
    result = (prime * result) + strength;
    return result;
  }

  public boolean isAttack() {
    return a;
  }

  public boolean isDefense() {
    return d;
  }

  public boolean isHP() {
    return h;
  }

  public boolean matches(final int inIVAttack, final int inIVDefense, final int inIVStamina) {
    int sum = inIVAttack + inIVDefense + inIVStamina;
    int maxIV = Math.max(inIVAttack, Math.max(inIVDefense, inIVStamina));
    boolean ta = a == (maxIV == inIVAttack);
    boolean td = d == (maxIV == inIVDefense);
    boolean ts = h == (maxIV == inIVStamina);
    boolean testSum = (sum >= min) && (sum <= max);
    boolean testStrength = (maxIV >= minStrength) && (maxIV <= maxStrength);
    boolean testIV = ta && td && ts;
    return testSum && testStrength && testIV;
  }

  @Override
  public String toString() {
    return Character.toString("x1234".charAt(level)) + "xdcba".charAt(strength) + "-" + (h ? "h" : "") + (a ? "a" : "") + (d ? "d" : "");
  }

  private void initialize(final int inLevel, final int inStrength, final boolean inHP, final boolean inAttack, final boolean inDefense) {
    level = inLevel;
    strength = inStrength;
    h = inHP;
    a = inAttack;
    d = inDefense;

    switch (inLevel) {
      case 4:
        max = 45;
        min = 37;
        break;
      case 3:
        max = 36;
        min = 30;
        break;
      case 2:
        max = 29;
        min = 23;
        break;
      case 1:
      case 0:
        level = 1;
        max = 22;
        min = 0;
        break;
      default:
        level = 0;
        max = 45;
        min = 0;
        break;
    }

    switch (inStrength) {
      case 4:
        maxStrength = 15;
        minStrength = 15;
        break;
      case 3:
        maxStrength = 14;
        minStrength = 13;
        break;
      case 2:
        maxStrength = 12;
        minStrength = 8;
        break;
      case 1:
        maxStrength = 7;
        minStrength = 0;
        break;
      default:
        strength = 0;
        maxStrength = 15;
        minStrength = 0;
        break;
    }
  }
}
