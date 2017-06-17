package net.ghielmetti.ivcalc.gui.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.utilities.Translations;

/**
 * A list renderer for the {@link CandidateDetail} object.
 *
 * @author Leopoldo Ghielmetti
 */
public class CandidateDetailRenderer implements ListCellRenderer<CandidateDetail> {
  private static final Color  GOOD                 = new Color(92, 255, 92);
  private static final Color  BAD                  = new Color(255, 92, 92);
  private static final Color  GOOD_SELECTED        = new Color(60, 166, 60);
  private static final Color  BAD_SELECTED         = new Color(166, 60, 60);
  private static final Color  HALF                 = new Color(255, 255, 0);
  private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

  @Override
  public Component getListCellRendererComponent(final JList<? extends CandidateDetail> inList, final CandidateDetail inCandidateDetail, final int inIndex, final boolean inIsSelected, final boolean inCellHasFocus) {
    JPanel panel = new JPanel(new GridLayout(1, 9));

    panel.setComponentOrientation(inList.getComponentOrientation());
    panel.setEnabled(inList.isEnabled());
    panel.setOpaque(true);

    Color bg = null;
    Color fg = null;
    boolean isSelected = inIsSelected;

    JList.DropLocation dropLocation = inList.getDropLocation();
    if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == inIndex) {

      bg = UIManager.getColor("List.dropCellBackground");
      fg = UIManager.getColor("List.dropCellForeground");

      isSelected = true;
    }

    if (isSelected) {
      panel.setBackground(bg == null ? inList.getSelectionBackground() : bg);
      panel.setForeground(fg == null ? inList.getSelectionForeground() : fg);
    } else {
      panel.setBackground(inList.getBackground());
      panel.setForeground(inList.getForeground());
    }

    Integer level = inCandidateDetail.getLevel();
    IVLevel ivLevel = inCandidateDetail.getIVLevel();

    JLabel labelLevel = new JLabel(Double.toString(level.doubleValue() / 2.0), SwingConstants.CENTER);

    if ((level.intValue() & 1) != 0) {
      labelLevel.setForeground(HALF);
      labelLevel.setFont(inList.getFont().deriveFont(Font.BOLD));
    } else {
      labelLevel.setForeground(inList.getForeground());
      labelLevel.setFont(inList.getFont());
    }

    JLabel labelCode = new JLabel(ivLevel.toString(), SwingConstants.CENTER);
    labelCode.setForeground(inList.getForeground());
    labelCode.setFont(inList.getFont());
    JLabel labelAttack = new JLabel(Integer.toString(ivLevel.getAttack()), SwingConstants.CENTER);
    labelAttack.setForeground(inList.getForeground());
    labelAttack.setFont(inList.getFont());
    JLabel labelDefense = new JLabel(Integer.toString(ivLevel.getDefense()), SwingConstants.CENTER);
    labelDefense.setForeground(inList.getForeground());
    labelDefense.setFont(inList.getFont());
    JLabel labelStamina = new JLabel(Integer.toString(ivLevel.getStamina()), SwingConstants.CENTER);
    labelStamina.setForeground(inList.getForeground());
    labelStamina.setFont(inList.getFont());
    JLabel labelPctIV = new JLabel(Translations.translate("pct", Integer.valueOf((int) (ivLevel.getTotal() / 45.0 * 100.0 + 0.5))), SwingConstants.CENTER);
    labelPctIV.setForeground(inList.getForeground());
    labelPctIV.setFont(inList.getFont());

    ImmutablePair<Integer, Integer> minMaxCP = inCandidateDetail.getMinMaxCP();
    double cpPct;

    if (minMaxCP.getLeft().equals(minMaxCP.getRight())) {
      cpPct = 1;
    } else {
      cpPct = (inCandidateDetail.getCP().doubleValue() - minMaxCP.getLeft().doubleValue()) / (minMaxCP.getRight().doubleValue() - minMaxCP.getLeft().doubleValue());
    }

    JLabel labelMinCP = new JLabel(minMaxCP.getLeft().toString(), SwingConstants.CENTER);
    labelMinCP.setForeground(inList.getForeground());
    labelMinCP.setFont(inList.getFont());
    JLabel labelMaxCP = new JLabel(minMaxCP.getRight().toString(), SwingConstants.CENTER);
    labelMaxCP.setForeground(inList.getForeground());
    labelMaxCP.setFont(inList.getFont());
    JLabel labelPctCP = new JLabel(Translations.translate("pct", Double.valueOf((int) (cpPct * 100000.0 + 0.5) / 1000.0)), SwingConstants.CENTER);
    labelPctCP.setForeground(inList.getForeground());
    labelPctCP.setFont(inList.getFont());

    if (isSelected) {
      panel.setBackground(inCandidateDetail.isBad() ? BAD_SELECTED : GOOD_SELECTED);
    } else {
      panel.setBackground(inCandidateDetail.isBad() ? BAD : GOOD);
    }

    panel.putClientProperty("candidate", ImmutablePair.of(level, ivLevel));

    panel.add(labelLevel);
    panel.add(labelCode);
    panel.add(labelAttack);
    panel.add(labelDefense);
    panel.add(labelStamina);
    panel.add(labelPctIV);
    panel.add(labelMinCP);
    panel.add(labelMaxCP);
    panel.add(labelPctCP);

    Border border = null;
    if (inCellHasFocus) {
      if (isSelected) {
        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("List.focusCellHighlightBorder");
      }
    } else {
      border = getNoFocusBorder();
    }

    panel.setBorder(border);
    return panel;
  }

  private Border getNoFocusBorder() {
    Border border = UIManager.getBorder("List.cellNoFocusBorder");
    if (border != null) {
      return border;
    }
    return SAFE_NO_FOCUS_BORDER;
  }
}
