package net.ghielmetti.ivcalc.data;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

/**
 * Checks if a Pokémon is good to be kept/improved/evolved.
 *
 * @author Leopoldo Ghielmetti
 */
public final class GoodnessChecker implements Serializable {
  private static final Pattern fromStringPattern = Pattern.compile("GoodnessChecker\\[name=\"([^\"]*)\", total=(\\d+), attack=(\\d+), defense=(\\d+), stamina=(\\d+)\\]");
  private final String         name;
  private int                  minTotal;
  private int                  minAttack;
  private int                  minDefense;
  private int                  minStamina;

  /**
   * Copy constructor.
   *
   * @param inName The name for this new checker.
   * @param inGoodnessChecker The template checker to copy.
   */
  public GoodnessChecker(final String inName, final GoodnessChecker inGoodnessChecker) {
    name = inName;
    minTotal = inGoodnessChecker.minTotal;
    minAttack = inGoodnessChecker.minAttack;
    minDefense = inGoodnessChecker.minDefense;
    minStamina = inGoodnessChecker.minStamina;
  }

  /**
   * Constructor.
   *
   * @param inName The name of this checker.
   * @param inMinTotal The minimum good total value.
   * @param inMinAttack The minimum good attack IV.
   * @param inMinDefense The minimum good defense IV.
   * @param inMinStamina The minimum good stamina IV.
   */
  public GoodnessChecker(final String inName, final int inMinTotal, final int inMinAttack, final int inMinDefense, final int inMinStamina) {
    name = inName;
    minTotal = inMinTotal;
    minAttack = inMinAttack;
    minDefense = inMinDefense;
    minStamina = inMinStamina;
  }

  /**
   * Creates a {@link GoodnessChecker} from the specified {@link String}.<br>
   * Used for deserialization.
   *
   * @param inString The {@link String} that represents a {@link GoodnessChecker}.
   * @return A new {@link GoodnessChecker}.
   */
  public static GoodnessChecker fromString(final String inString) {
    try {
      Matcher m = fromStringPattern.matcher(inString);

      if (m.matches()) {
        String name = m.group(1);
        int total = Integer.parseInt(m.group(2));
        int attack = Integer.parseInt(m.group(3));
        int defense = Integer.parseInt(m.group(4));
        int stamina = Integer.parseInt(m.group(5));
        return new GoodnessChecker(name, total, attack, defense, stamina);
      }
    } catch (Exception e) {
      LoggerFactory.getLogger(GoodnessChecker.class).warn("Invalid stored configuration found: ", inString, e);
    }

    return null;
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
    GoodnessChecker other = (GoodnessChecker) obj;
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
   * Getter for the min attack property.
   *
   * @return The value.
   */
  public int getMinAttack() {
    return minAttack;
  }

  /**
   * Getter for the min defense property.
   *
   * @return The value.
   */
  public int getMinDefense() {
    return minDefense;
  }

  /**
   * Getter for the min stamina property.
   *
   * @return The value.
   */
  public int getMinStamina() {
    return minStamina;
  }

  /**
   * Getter for the min total property.
   *
   * @return The value.
   */
  public int getMinTotal() {
    return minTotal;
  }

  /**
   * Getter for the name of this checker.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
    return result;
  }

  /**
   * Checks if the given {@link IVLevel} is good for this checker.
   *
   * @param inIVLevel The {@link IVLevel} to check.
   * @return <code>true</code> if it's good.
   */
  public boolean isGood(final IVLevel inIVLevel) {
    return inIVLevel.getTotal() >= minTotal //
        && inIVLevel.getStamina() >= minStamina //
        && inIVLevel.getAttack() >= minAttack //
        && inIVLevel.getDefense() >= minDefense;
  }

  /**
   * Setter for the min attack property.<br>
   * It must be between 0 and 15 (included).
   *
   * @param inMinAttack the value.
   */
  public void setMinAttack(final int inMinAttack) {
    if (inMinAttack < 0) {
      minAttack = 0;
    } else if (inMinAttack > 15) {
      minAttack = 15;
    } else {
      minAttack = inMinAttack;
    }
  }

  /**
   * Setter for the min defense property.<br>
   * It must be between 0 and 15 (included).
   *
   * @param inMinDefense the value.
   */
  public void setMinDefense(final int inMinDefense) {
    if (inMinDefense < 0) {
      minDefense = 0;
    } else if (inMinDefense > 15) {
      minDefense = 15;
    } else {
      minDefense = inMinDefense;
    }
  }

  /**
   * Setter for the min stamina property.<br>
   * It must be between 0 and 15 (included).
   *
   * @param inMinStamina the value.
   */
  public void setMinStamina(final int inMinStamina) {
    if (inMinStamina < 0) {
      minStamina = 0;
    } else if (inMinStamina > 15) {
      minStamina = 15;
    } else {
      minStamina = inMinStamina;
    }
  }

  /**
   * Setter for the min total property.<br>
   * It must be between 0 and 15 (included).
   *
   * @param inMinTotal the value.
   */
  public void setMinTotal(final int inMinTotal) {
    if (inMinTotal < 0) {
      minTotal = 0;
    } else if (inMinTotal > 45) {
      minTotal = 45;
    } else {
      minTotal = inMinTotal;
    }
  }

  @Override
  public String toString() {
    return "GoodnessChecker[name=\"" + name + "\", total=" + minTotal + ", attack=" + minAttack + ", defense=" + minDefense + ", stamina=" + minStamina + "]";
  }
}
