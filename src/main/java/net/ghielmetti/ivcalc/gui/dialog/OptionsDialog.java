package net.ghielmetti.ivcalc.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import net.ghielmetti.ivcalc.gui.model.GoodnessCheckerModelIfc;
import net.ghielmetti.ivcalc.gui.panel.GoodnessCheckerPanel;
import net.ghielmetti.utilities.Translations;

/**
 * The dialog that allow the user to change the program options.
 *
 * @author Leopoldo Ghielmetti
 */
public class OptionsDialog extends JDialog {
  private GoodnessCheckerModelIfc checkerModel;

  /**
   * Constructor.
   *
   * @param inOwner The owner of this dialog.
   * @param inModel The {@link GoodnessCheckerModelIfc}.
   */
  public OptionsDialog(final Frame inOwner, final GoodnessCheckerModelIfc inModel) {
    super(inOwner, Translations.translate("dialog.options.title"), false);

    checkerModel = inModel;

    initialize(inOwner);
  }

  private void initialize(final Frame inOwner) {
    JPanel panel = new JPanel(new BorderLayout());
    GoodnessCheckerPanel checkerPanel = new GoodnessCheckerPanel(checkerModel);
    panel.add(checkerPanel);

    JPanel buttonsSouth = new JPanel();
    JButton buttonSave = new JButton(Translations.translate("default.button.save"));
    JButton buttonCancel = new JButton(Translations.translate("default.button.cancel"));
    buttonsSouth.add(buttonSave);
    buttonsSouth.add(buttonCancel);
    buttonSave.addActionListener(e -> {
      checkerModel.save();
      dispose();
    });
    buttonCancel.addActionListener(e -> dispose());

    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(buttonsSouth, BorderLayout.SOUTH);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(true);
    pack();
    setLocationRelativeTo(inOwner);
  }

}
