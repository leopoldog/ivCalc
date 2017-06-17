package net.ghielmetti.ivcalc.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.ghielmetti.ivcalc.data.GoodnessChecker;
import net.ghielmetti.ivcalc.gui.model.GoodnessCheckerModelIfc;
import net.ghielmetti.utilities.Translations;

/**
 * A {@link JPanel} that displays a list of all known {@link GoodnessChecker}s and allow the user to change the values
 * and the {@link GoodnessChecker}s.
 *
 * @author Leopoldo Ghielmetti
 */
public class GoodnessCheckerPanel extends JPanel implements TableModelListener {
  private JTable                  table;
  private GoodnessCheckerModelIfc model;

  /**
   * Constructor.
   *
   * @param inModel The model for this panel.
   */
  public GoodnessCheckerPanel(final GoodnessCheckerModelIfc inModel) {
    super(new BorderLayout());
    model = inModel;
    model.addTableModelListener(this);
    initialize();
  }

  @Override
  public void tableChanged(final TableModelEvent inEvent) {
    if (inEvent.getColumn() == -1) {
      table.updateUI();
    }
  }

  private void initialize() {
    JPanel panelEast = new JPanel(new BorderLayout());
    JPanel buttonsEast = new JPanel(new GridLayout(2, 1, 10, 10));
    JButton buttonAdd = new JButton(Translations.translate("default.button.add"));
    JButton buttonRemove = new JButton(Translations.translate("default.button.remove"));
    buttonsEast.add(buttonAdd);
    buttonsEast.add(buttonRemove);
    buttonAdd.addActionListener(e -> showPokemonSelector());
    buttonRemove.addActionListener(e -> model.deleteChecker(table.getSelectedRow()));
    panelEast.add(buttonsEast, BorderLayout.NORTH);
    panelEast.setBorder(new EmptyBorder(10, 0, 0, 10));

    table = new JTable(model);
    JScrollPane scroll = new JScrollPane(table);
    add(scroll, BorderLayout.CENTER);
    add(panelEast, BorderLayout.EAST);
  }

  private void showPokemonSelector() {
    List<String> names = model.getAllAvailableNames();

    String choosed = (String) JOptionPane.showInputDialog(this, Translations.translate("dialog.options.choose.available.pokemons"), Translations.translate("dialog.options.choose.pokemon"), JOptionPane.QUESTION_MESSAGE, null,
        names.toArray(new String[names.size()]), null);

    if (choosed != null) {
      model.createChecker(choosed);
    }
  }
}
