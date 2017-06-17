package pokemon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import net.ghielmetti.pokemon.Item;

public class ItemTest {
  private Item item;

  @Test
  public void getBaseAttack_always_returnsTheBaseAttackValue() {
    assertEquals(116, item.getBaseAttack());
  }

  @Test
  public void getBaseDefense_always_returnsTheBaseDefenseValue() {
    assertEquals(96, item.getBaseDefense());
  }

  @Test
  public void getBaseStamina_always_returnsTheBaseStaminaValue() {
    assertEquals(78, item.getBaseStamina());
  }

  @Test
  public void getCP_always_returnsTheCPForTheItem() {
    assertEquals(527, item.getCP(4000, 11, 7, 6).get(Integer.valueOf(50)).intValue());
  }

  @Before
  public void setUp() {
    item = new Item("Charmander", 116, 96, 78);
  }

  @Test
  public void toString_always_returnsTheDescription() {
    assertEquals("Charmander:\n  Base attack : 116\n  Base defense:  96\n  Base stamina:  78", item.toString());
  }
}
