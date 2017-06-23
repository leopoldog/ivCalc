package net.ghielmetti.pokemon;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import net.ghielmetti.utilities.Pair;

public class CandidatePanel extends JPanel {
  private static final Color GOOD      = new Color(150, 255, 150);
  private static final Color BAD       = new Color(255, 150, 150);
  private static final Color HALF_GOOD = new Color(224, 255, 150);
  private static final Color HALF_BAD  = new Color(255, 203, 150);

  public CandidatePanel(final Item inItem, final Integer inCP, final List<Pair<Integer, IVLevel>> inCandidates) {
    super(new GridBagLayout());
    setBorder(new BevelBorder(BevelBorder.LOWERED));
    Pair<Integer, Integer> minMaxCP40 = inItem.getMinMaxCP(80);
    add(new JLabel(inItem.getName(), inItem.getIcon(), SwingConstants.LEFT), new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    add(new JLabel("@Level 40 CP=", SwingConstants.RIGHT), new GridBagConstraints(6, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(new JLabel(minMaxCP40.getRight().toString(), SwingConstants.LEFT), new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    add(toLabel("Level", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("Code", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("IV Attack", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("IV Defense", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("IV Stamina", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(4, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("%IV", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("MinCP", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(6, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("MaxCP", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(7, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(toLabel("%CP", Color.LIGHT_GRAY, SwingConstants.CENTER), new GridBagConstraints(8, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    int y = 2;
    for (Pair<Integer, IVLevel> candidate : inCandidates) {
      Color bgColor;
      boolean isBad = ((candidate.getRight().getAttack() < 10) || (candidate.getRight().getDefense() < 10) || (candidate.getRight().getStamina() < 10));

      if ((candidate.getLeft().intValue() & 1) != 0) {
        bgColor = isBad ? HALF_BAD : HALF_GOOD;
      } else {
        bgColor = isBad ? BAD : GOOD;
      }

      add(toLabel(Double.toString(candidate.getLeft().doubleValue() / 2.0), bgColor, SwingConstants.CENTER), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel(candidate.getRight().toString(), bgColor, SwingConstants.CENTER), new GridBagConstraints(1, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel(Integer.toString(candidate.getRight().getAttack()), bgColor, SwingConstants.CENTER), new GridBagConstraints(2, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel(Integer.toString(candidate.getRight().getDefense()), bgColor, SwingConstants.CENTER), new GridBagConstraints(3, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel(Integer.toString(candidate.getRight().getStamina()), bgColor, SwingConstants.CENTER), new GridBagConstraints(4, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel("%" + Integer.toString((int) (((candidate.getRight().getTotal() / 45.0) * 100.0) + 0.5)), bgColor, SwingConstants.CENTER),
          new GridBagConstraints(5, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

      Pair<Integer, Integer> minMaxCP = inItem.getMinMaxCP(candidate.getLeft().intValue());
      double cpPct = (inCP.doubleValue() - minMaxCP.getLeft().doubleValue()) / (minMaxCP.getRight().doubleValue() - minMaxCP.getLeft().doubleValue());

      add(toLabel(minMaxCP.getLeft().toString(), bgColor, SwingConstants.CENTER), new GridBagConstraints(6, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel(minMaxCP.getRight().toString(), bgColor, SwingConstants.CENTER), new GridBagConstraints(7, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      add(toLabel("%" + Double.toString((int) ((cpPct * 100000.0) + 0.5) / 1000.0), bgColor, SwingConstants.CENTER), new GridBagConstraints(8, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      y++;
    }
  }

  private JLabel toLabel(final String inText, final Color inBgColor, final int inAlign) {
    JLabel l = new JLabel(inText, inAlign);
    if (inBgColor != null) {
      l.setBackground(inBgColor);
      l.setOpaque(true);
    }
    return l;
  }
}
