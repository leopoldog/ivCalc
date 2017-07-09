package net.ghielmetti.pokemon;

public class IVLevel implements Comparable<IVLevel> {
  private int sum;
  private int attack;
  private int defense;
  private int stamina;

  public IVLevel(final int inIVAttack, final int inIVDefense, final int inIVStamina) {
    attack = inIVAttack;
    defense = inIVDefense;
    stamina = inIVStamina;
    sum = attack + defense + stamina;
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
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    IVLevel other = (IVLevel) obj;
    return (attack == other.attack) && (defense == other.defense) && (stamina == other.stamina);
  }

  public int getAttack() {
    return attack;
  }

  public int getDefense() {
    return defense;
  }

  public int getStamina() {
    return stamina;
  }

  public int getTotal() {
    return attack + defense + stamina;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + attack;
    result = (prime * result) + defense;
    result = (prime * result) + stamina;
    return result;
  }

  @Override
  public String toString() {
    return String.format("%02d-%x%x%x", Integer.valueOf(attack + defense + stamina), Integer.valueOf(attack), Integer.valueOf(defense), Integer.valueOf(stamina));
  }
}
