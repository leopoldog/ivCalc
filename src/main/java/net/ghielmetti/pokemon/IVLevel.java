package net.ghielmetti.pokemon;

public class IVLevel {
  private int attack;
  private int defense;
  private int stamina;

  public IVLevel(final int inIVAttack, final int inIVDefense, final int inIVStamina) {
    attack = inIVAttack;
    defense = inIVDefense;
    stamina = inIVStamina;
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

  @Override
  public String toString() {
    return String.format("%02d-%x%x%x", Integer.valueOf(attack + defense + stamina), Integer.valueOf(attack), Integer.valueOf(defense), Integer.valueOf(stamina));
  }
}
