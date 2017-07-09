package net.ghielmetti.pokemon.team;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class TeamDialog extends JDialog {
  private Team team;

  public TeamDialog(final JFrame inOwner) {
    super(inOwner, true);

    getContentPane().setLayout(new GridLayout(Team.values().length, 1));

    for (Team t : Team.values()) {
      JButton button = new JButton(t.getTeamName());
      button.setForeground(t.getColor());
      button.addActionListener(l -> {
        team = t;
        dispose();
      });
      getContentPane().add(button);
    }

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);
    setLocationRelativeTo(inOwner);
    pack();
  }

  public Team getTeam() {
    return team;
  }
}
