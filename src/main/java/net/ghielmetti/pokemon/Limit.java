package net.ghielmetti.pokemon;

public class Limit {
  private char    level;
  private int     min;
  private int     max;
  private char    strength;
  private int     minStrength;
  private int     maxStrength;
  private boolean a;
  private boolean h;
  private boolean d;

  public Limit(final String inDescription) {
    String description = inDescription.toLowerCase();
    level = description.charAt(0);

    switch (level) {
      case '0':
      case '4':
        max = 45;
        min = 37;
        break;
      case '3':
        max = 36;
        min = 30;
        break;
      case '2':
        max = 29;
        min = 23;
        break;
      case '1':
        level = '1';
        max = 22;
        min = 0;
        break;
      default:
        max = 45;
        min = 0;
        break;
    }

    strength = description.charAt(1);
    switch (strength) {
      case 'a':
        maxStrength = 15;
        minStrength = 15;
        break;
      case 'b':
        maxStrength = 14;
        minStrength = 13;
        break;
      case 'c':
        maxStrength = 12;
        minStrength = 8;
        break;
      case 'd':
        maxStrength = 7;
        minStrength = 0;
        break;
      default:
        strength = 'x';
        maxStrength = 15;
        minStrength = 0;
        break;
    }

    String detail = description.substring(2);
    a = detail.indexOf('a') != -1;
    h = detail.indexOf('h') != -1;
    d = detail.indexOf('d') != -1;
  }

  public boolean canBe(final int inIVA, final int inIVS) {
    int sum = inIVA + inIVS;
    return ((sum + 15) >= min) && (sum <= max);
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
    return Character.toString(level) + strength + "-" + (h ? "h" : "") + (a ? "a" : "") + (d ? "d" : "");
  }
}
