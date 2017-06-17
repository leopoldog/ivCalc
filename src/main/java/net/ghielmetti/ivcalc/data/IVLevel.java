package net.ghielmetti.ivcalc.data;

/**
 * This object represent a group of IV that makes a Pokémon possibility.<br>
 * Each IV can take a value from 0 to 15 and together makes a possibility. So there are 4096 different {@link IVLevel}s.
 *
 * @author Leopoldo Ghielmetti
 */
public final class IVLevel implements Comparable<IVLevel> {
  private final int     sum;
  private final int     attack;
  private final int     defense;
  private final int     stamina;
  private final boolean isAttack;
  private final boolean isDefense;
  private final boolean isStamina;
  private final int     maxIV;

  /**
   * Constructor for an {@link IVLevel}
   *
   * @param inIVAttack The attack IV (between 0 and 15)
   * @param inIVDefense The defense IV (between 0 and 15)
   * @param inIVStamina The stamina IV (between 0 and 15)
   */
  public IVLevel(final int inIVAttack, final int inIVDefense, final int inIVStamina) {
    assert inIVAttack >= 0 && inIVAttack < 16;
    assert inIVDefense >= 0 && inIVDefense < 16;
    assert inIVStamina >= 0 && inIVStamina < 16;

    attack = inIVAttack;
    defense = inIVDefense;
    stamina = inIVStamina;
    sum = attack + defense + stamina;

    maxIV = Math.max(attack, Math.max(defense, stamina));
    isAttack = maxIV == attack;
    isDefense = maxIV == defense;
    isStamina = maxIV == stamina;
  }

  @Override
  public int compareTo(final IVLevel inLevel) {
    if (sum < inLevel.sum) {
      return -1;
    } else if (sum > inLevel.sum) {
      return 1;
    }

    if (attack < inLevel.attack) {
      return -1;
    } else if (attack > inLevel.attack) {
      return 1;
    }

    if (defense < inLevel.defense) {
      return -1;
    } else if (defense > inLevel.defense) {
      return 1;
    }

    if (stamina < inLevel.stamina) {
      return -1;
    } else if (stamina > inLevel.stamina) {
      return 1;
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
    IVLevel other = (IVLevel) obj;
    return attack == other.attack && defense == other.defense && stamina == other.stamina;
  }

  /**
   * Returns the attack IV.
   *
   * @return The attack IV
   */
  public int getAttack() {
    return attack;
  }

  /**
   * Returns the defense IV.
   *
   * @return The defense IV
   */
  public int getDefense() {
    return defense;
  }

  /**
   * Returns the max value for IV.
   *
   * @return the max.
   */
  public int getMaxIV() {
    return maxIV;
  }

  /**
   * Returns the stamina IV.
   *
   * @return The stamina IV
   */
  public int getStamina() {
    return stamina;
  }

  /**
   * Returns the total of the IVs.<br>
   * the sum takes values from 0 (i.e. 0%) up to 45 (i.e. 100%). To know the percentage of this IVLevel it must be
   * multiplied by 2.22...
   *
   * @return The sum of the IVs
   */
  public int getTotal() {
    return attack + defense + stamina;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + attack;
    result = prime * result + defense;
    result = prime * result + stamina;
    return result;
  }

  /**
   * Returns true if the attack has the highest value.
   *
   * @return <code>true</code> if it's the strongest.
   */
  public boolean isAttack() {
    return isAttack;
  }

  /**
   * Returns true if the defense has the highest value.
   *
   * @return <code>true</code> if it's the strongest.
   */
  public boolean isDefense() {
    return isDefense;
  }

  /**
   * Returns true if the stamina has the highest value.
   *
   * @return <code>true</code> if it's the strongest.
   */
  public boolean isStamina() {
    return isStamina;
  }

  @Override
  public String toString() {
    return String.format("%02d-%x%x%x", Integer.valueOf(attack + defense + stamina), Integer.valueOf(attack), Integer.valueOf(defense), Integer.valueOf(stamina));
  }
}
