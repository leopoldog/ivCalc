package net.ghielmetti.ivcalc.gui.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.pokedex.Type;
import net.ghielmetti.utilities.Translations;

/**
 * A list renderer for the {@link CandidateList} object.
 *
 * @author Leopoldo Ghielmetti
 */
public class CandidateListRenderer implements ListCellRenderer<CandidateList> {
  private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

  @Override
  public Component getListCellRendererComponent(final JList<? extends CandidateList> inList, final CandidateList inCandidateList, final int inIndex, final boolean inIsSelected, final boolean inCellHasFocus) {
    JPanel panel = new JPanel(new GridBagLayout());

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

    Pokemon pokemon = inCandidateList.getPokemon();
    Type[] types = pokemon.getTypes();

    JLabel type1Label = new JLabel(types[0].getTypeName(), types[0].getIcon(), SwingConstants.CENTER);
    type1Label.setBackground(types[0].getColor());
    type1Label.setOpaque(true);
    type1Label.setForeground(inList.getForeground());
    type1Label.setFont(inList.getFont());

    JLabel type2Label;

    if (types.length > 1) {
      type2Label = new JLabel(types[1].getTypeName(), types[1].getIcon(), SwingConstants.CENTER);
      type2Label.setBackground(types[1].getColor());
      type2Label.setOpaque(true);
    } else {
      type2Label = new JLabel("");
    }

    type2Label.setForeground(inList.getForeground());
    type2Label.setFont(inList.getFont());

    JLabel iconLabel = new JLabel(pokemon.getName() + " (#" + pokemon.getNumber() + ")", pokemon.getIcon(), SwingConstants.LEFT);
    iconLabel.setForeground(inList.getForeground());
    iconLabel.setFont(inList.getFont());

    JLabel foundLabel;

    if (inCandidateList.size() == 1) {
      foundLabel = new JLabel(Translations.translate("candidate.found"), SwingConstants.RIGHT);
    } else {
      foundLabel = new JLabel(Translations.translate("candidates.found", Integer.toString(inCandidateList.size())), SwingConstants.RIGHT);
    }

    foundLabel.setForeground(inList.getForeground());
    foundLabel.setFont(inList.getFont());

    panel.add(iconLabel, new GridBagConstraints(0, 0, 1, 3, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panel.add(new JLabel(""), new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 5, 5));
    panel.add(type1Label, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 5, 5));
    panel.add(new JLabel(""), new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 5, 5));
    panel.add(type2Label, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 5, 5));
    panel.add(foundLabel, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

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
