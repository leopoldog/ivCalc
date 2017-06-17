package net.ghielmetti.ivcalc.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.ghielmetti.ivcalc.pokedex.Team;
import net.ghielmetti.utilities.Translations;

/**
 * A {@link JDialog} to ask for the Pokémon team.
 *
 * @author Leopoldo Ghielmetti
 */
public class TeamDialog extends JDialog {
  private Team team;

  /**
   * Constructor.
   *
   * @param inOwner The owner of the {@link JDialog}.
   */
  public TeamDialog(final JFrame inOwner) {
    super(inOwner, Translations.translate("dialog.chooseTeam.title"), true);

    initialize(inOwner);
  }

  /**
   * Returns the choosen {@link Team}.
   *
   * @return The {@link Team}.
   */
  public Team getTeam() {
    return team;
  }

  private void initialize(final JFrame inOwner) {
    JPanel panel = new JPanel(new GridLayout(Team.values().length + 1, 2, 5, 5));
    JLabel leaderTitle = new JLabel(Translations.translate("dialog.chooseTeam.leader"), SwingConstants.CENTER);
    JLabel teamTitle = new JLabel(Translations.translate("dialog.chooseTeam.team"), SwingConstants.CENTER);
    leaderTitle.setFont(leaderTitle.getFont().deriveFont(Font.BOLD, 20));
    teamTitle.setFont(leaderTitle.getFont());
    panel.add(leaderTitle);
    panel.add(teamTitle);

    for (Team t : Team.values()) {
      t.initialize();
      JLabel label = new JLabel(t.getLeaderName());
      JButton button = new JButton(t.getTeamName());
      label.setForeground(t.getColor());
      button.setForeground(t.getColor());
      button.addActionListener(e -> {
        team = t;
        dispose();
      });
      panel.add(label);
      panel.add(button);
    }

    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    getContentPane().add(panel, BorderLayout.CENTER);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);
    setLocationRelativeTo(inOwner);
    pack();
  }
}
