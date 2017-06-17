package net.ghielmetti.ivcalc.gui.dialog;

import java.awt.Frame;
import java.util.Locale;

import net.ghielmetti.utilities.swing.JLanguageDialog;

/**
 * A dialog window that changes the default Locale.
 *
 * @author Leopoldo Ghielmetti
 */
public class LanguageDialog extends JLanguageDialog {
  /**
   * Constructor.
   *
   * @param inOwner The owner of this window.
   */
  public LanguageDialog(final Frame inOwner) {
    super(inOwner);
  }

  @Override
  protected Locale[] getAvailableLocales() {
    return new Locale[]{Locale.FRENCH, Locale.ITALIAN, Locale.ENGLISH, Locale.JAPANESE, Locale.GERMAN};
  }
}
