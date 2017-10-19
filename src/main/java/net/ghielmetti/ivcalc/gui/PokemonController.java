package net.ghielmetti.ivcalc.gui;

import java.awt.Desktop;
import java.awt.Font;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.LatestRelease;
import net.ghielmetti.ivcalc.gui.dialog.LanguageDialog;
import net.ghielmetti.ivcalc.gui.dialog.OptionsDialog;
import net.ghielmetti.ivcalc.gui.dialog.TeamDialog;
import net.ghielmetti.ivcalc.gui.model.GoodnessCheckerModel;
import net.ghielmetti.ivcalc.gui.model.PokemonModel;
import net.ghielmetti.ivcalc.pokedex.Pokedex;
import net.ghielmetti.ivcalc.pokedex.Team;
import net.ghielmetti.ivcalc.pokedex.Type;
import net.ghielmetti.ivcalc.tools.PreferenceStore;
import net.ghielmetti.utilities.Translations;
import net.ghielmetti.utilities.swing.JLanguageDialog;

/**
 * The main controller for the Pokémon application.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonController implements Observer {
  private static final Logger    LOGGER = LoggerFactory.getLogger(PokemonController.class);
  private static final ImageIcon LOGO   = new ImageIcon(ClassLoader.getSystemResource("icons/logo.png"));
  private static final String    TITLE  = "Pokémon GO IV Calculator";
  private Team                   team;
  private PokemonView            view;
  private PokemonModel           model;

  /** Starts the controller. */
  public void start() {
    try {
      if (getTeam() != null) {
        Translations.initialize();
        team.initialize();
        Pokedex pokedex = new Pokedex();
        model = new PokemonModel(team, pokedex, new GoodnessCheckerModel(pokedex));
        model.addObserver(this);
        view = new PokemonView(model, e -> searchPokemons(), e -> askForTeam(), e -> showAbout(), e -> showOptions(), e -> askForLanguage());
        view.setTitle(TITLE + " version " + model.getVersion() + ", team: " + team.getTeamName() + " (Leader: " + team.getLeaderName() + ")");
        view.setIconImage(LOGO.getImage());
        view.reset();
        view.setVisible(true);

        new Thread(() -> {
          try {
            String latest = LatestRelease.getLatestVersion();
            if (!latest.isEmpty() && !latest.equals("v" + model.getVersion())) {
              view.setTitle(view.getTitle() + " " + Translations.translate("new.version.available"));
            }
          } catch (Exception e) {
            // silently ignore
          }
        }, "Check for updates").start();
      }
    } catch (Exception e) {
      LOGGER.error("Error in execution", e);
    }
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    // nothing to do
  }

  private void askForLanguage() {
    JLanguageDialog dialog = new LanguageDialog(view);

    dialog.setVisible(true);

    Locale locale = dialog.getLanguage();

    if (locale != null) {
      Locale.setDefault(locale);
      PreferenceStore.setLocale(locale);
      view.dispose();
      Type.initialize();
      new PokemonController().start();
    }
  }

  private void askForTeam() {
    TeamDialog dialog = new TeamDialog(view);

    dialog.setVisible(true);

    Team newTeam = dialog.getTeam();

    if (newTeam != null) {
      PreferenceStore.setTeam(newTeam);
      team = newTeam;

      if (view != null) {
        view.dispose();
        new PokemonController().start();
      }
    }
  }

  private Team getTeam() {
    team = PreferenceStore.getTeam();
    if (team == null) {
      LOGGER.debug("Invalid team name, asking for the team");
      askForTeam();
    }
    return team;
  }

  private void openURL(final HyperlinkEvent inEvent) {
    try {
      if (HyperlinkEvent.EventType.ACTIVATED.equals(inEvent.getEventType())) {
        Desktop.getDesktop().browse(inEvent.getURL().toURI());
      }
    } catch (Exception e) {
      LOGGER.error("Unable to browse", e);
    }
  }

  private synchronized void searchPokemons() {
    SwingUtilities.invokeLater(() -> {
      try {
        view.lockButtons(true);
        model.searchCandidates();
      } finally {
        try {
          view.reset();
        } catch (Exception e) {
          LOGGER.warn("Unexpected", e);
        } finally {
          view.lockButtons(false);
        }
      }
    });
  }

  private void showAbout() {
    try {
      JLabel label = new JLabel();
      Font font = label.getFont();
      StringBuilder style = new StringBuilder("font-family:" + font.getFamily() + ";");
      style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
      style.append("font-size:" + font.getSize() + "pt;");
      JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" + TITLE //
          + "<br>Version: " + model.getVersion() //
          + "<br>Copyright 2017 by LGS (Leopoldo Ghielmetti Software)" //
          + "<br>Distributed under GPLv3!" //
          + "<br>The sources are available on GitHub: <a href=\"https://github.com/leopoldog/ivCalc\">ivCalc</a> and <a href=\"https://github.com/leopoldog/utilities\">Utilities</a>." //
          + "</body></html>");
      ep.addHyperlinkListener(this::openURL);
      ep.setEditable(false);
      ep.setBackground(label.getBackground());
      JOptionPane.showMessageDialog(view, ep, TITLE, JOptionPane.PLAIN_MESSAGE, LOGO);
    } catch (Exception e) {
      LOGGER.error("Unable to open the About dialog.", e);
    }
  }

  private void showOptions() {
    OptionsDialog dialog = new OptionsDialog(view, model.getGoodnessModel());

    dialog.setVisible(true);
  }
}
