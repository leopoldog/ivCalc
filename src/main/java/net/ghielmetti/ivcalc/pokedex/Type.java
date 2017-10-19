package net.ghielmetti.ivcalc.pokedex;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Pokémon types.
 *
 * @author Leopoldo Ghielmetti
 */
public enum Type {
  /***/
  BUG(new Color(0xA8, 0xB8, 0x20)),
  /***/
  DARK(new Color(0x70, 0x58, 0x48)),
  /***/
  DRAGON(new Color(0x70, 0x38, 0xF8)),
  /***/
  ELECTRIC(new Color(0xF8, 0xD0, 0x30)),
  /***/
  FAIRY(new Color(0xEE, 0x99, 0xAC)),
  /***/
  FIGHTING(new Color(0xC0, 0x30, 0x28)),
  /***/
  FIRE(new Color(0xF0, 0x80, 0x30)),
  /***/
  FLYING(new Color(0xA8, 0x90, 0xF0)),
  /***/
  GHOST(new Color(0x70, 0x58, 0x98)),
  /***/
  GRASS(new Color(0x78, 0xC8, 0x50)),
  /***/
  GROUND(new Color(0xE0, 0xC0, 0x68)),
  /***/
  ICE(new Color(0x98, 0xD8, 0xD8)),
  /***/
  NORMAL(new Color(0xA8, 0xA8, 0x78)),
  /***/
  POISON(new Color(0xA0, 0x40, 0xA0)),
  /***/
  PSYCHIC(new Color(0xF8, 0x58, 0x88)),
  /***/
  ROCK(new Color(0xB8, 0xA0, 0x38)),
  /***/
  STEEL(new Color(0xB8, 0xB8, 0xD0)),
  /***/
  WATER(new Color(0x68, 0x90, 0xF0));

  private static final String RESOURCE_TYPE_NAMES = "typeNames";

  static {
    initialize();
  }

  private static ResourceBundle bundle;
  private Color                 color;
  private Icon                  icon;

  private Type(final Color inColor) {
    color = inColor;

    URL iconURL = getClass().getResource("/types/" + name() + ".png");

    if (iconURL == null) {
      icon = null;
    } else {
      icon = new ImageIcon(new ImageIcon(iconURL).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
    }
  }

  /**
   * Initializes the Type translations. This method can be called when the default Locale is changed to reload the
   * translations.
   */
  public static void initialize() {
    bundle = ResourceBundle.getBundle(RESOURCE_TYPE_NAMES);
  }

  /**
   * Returns the corresponding type's {@link Color}.
   *
   * @return The color.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the icon of this type.
   *
   * @return The icon.
   */
  public Icon getIcon() {
    return icon;
  }

  /**
   * Returns the corresponding type's name.
   *
   * @return The name.
   */
  public String getTypeName() {
    return bundle.getString(name());
  }
}
