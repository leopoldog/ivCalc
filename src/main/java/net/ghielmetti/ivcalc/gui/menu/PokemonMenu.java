package net.ghielmetti.ivcalc.gui.menu;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import net.ghielmetti.utilities.Translations;

/**
 * The menu for the Pokémon application.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonMenu extends JMenuBar {
  /**
   * Constructor.
   *
   * @param inChangeTeamListener Called when the user asks to change the team.
   * @param inChangeLanguageListener Called when the user asks to change the language.
   * @param inShowOptionsListener Called when the user asks to change the search options.
   * @param inShowAboutListener Called when the user asks to see the about dialog.
   * @param inExitListener Called when the user wants to exit the application.
   */
  public PokemonMenu(final ActionListener inChangeTeamListener, final ActionListener inChangeLanguageListener, final ActionListener inShowOptionsListener, final ActionListener inShowAboutListener, final ActionListener inExitListener) {
    JMenu fileMenu = new JMenu(Translations.translate("menu.file.label"));
    fileMenu.setMnemonic(Translations.translate("menu.file.shortcut").toUpperCase().charAt(0));

    JMenu helpMenu = new JMenu(Translations.translate("menu.help.label"));
    helpMenu.setMnemonic(Translations.translate("menu.help.shortcut").toUpperCase().charAt(0));

    addChangeTeam(fileMenu, inChangeTeamListener);
    addChangeLanguage(fileMenu, inChangeLanguageListener);
    addOptions(fileMenu, inShowOptionsListener);
    addExit(fileMenu, inExitListener);
    addAbout(helpMenu, inShowAboutListener);

    if (fileMenu.getItemCount() > 0) {
      add(fileMenu);
    }

    if (helpMenu.getItemCount() > 0) {
      add(Box.createHorizontalGlue());
      add(helpMenu);
    }
  }

  private void addAbout(final JMenu inMenu, final ActionListener inShowAboutListener) {
    if (inShowAboutListener != null) {
      JMenuItem item = new JMenuItem(Translations.translate("menu.about.label"));
      item.setMnemonic(Translations.translate("menu.about.shortcut").toUpperCase().charAt(0));
      item.addActionListener(inShowAboutListener);
      inMenu.add(item);
    }
  }

  private void addChangeLanguage(final JMenu inMenu, final ActionListener inChangeLanguageListener) {
    if (inChangeLanguageListener != null) {
      JMenuItem item = new JMenuItem(Translations.translate("menu.language.label"));
      item.setMnemonic(Translations.translate("menu.language.shortcut").toUpperCase().charAt(0));
      item.addActionListener(inChangeLanguageListener);
      inMenu.add(item);
    }
  }

  private void addChangeTeam(final JMenu inMenu, final ActionListener inChangeTeamListener) {
    if (inChangeTeamListener != null) {
      JMenuItem item = new JMenuItem(Translations.translate("menu.team.label"));
      item.setMnemonic(Translations.translate("menu.team.shortcut").toUpperCase().charAt(0));
      item.addActionListener(inChangeTeamListener);
      inMenu.add(item);
    }
  }

  private void addExit(final JMenu inMenu, final ActionListener inExitListener) {
    if (inExitListener != null) {
      JMenuItem item = new JMenuItem(Translations.translate("menu.exit.label"));
      item.setMnemonic(Translations.translate("menu.exit.shortcut").toUpperCase().charAt(0));
      item.addActionListener(inExitListener);
      inMenu.add(item);
    }
  }

  private void addOptions(final JMenu inMenu, final ActionListener inShowOptionsListener) {
    if (inShowOptionsListener != null) {
      JMenuItem item = new JMenuItem(Translations.translate("menu.options.label"));
      item.setMnemonic(Translations.translate("menu.options.shortcut").toUpperCase().charAt(0));
      item.addActionListener(inShowOptionsListener);
      inMenu.add(item);
    }
  }
}
