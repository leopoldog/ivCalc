package net.ghielmetti.ivcalc.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.gui.model.ValuesModelIfc;
import net.ghielmetti.ivcalc.pokedex.IVMap;
import net.ghielmetti.ivcalc.pokedex.Multiplier;
import net.ghielmetti.utilities.Translations;
import net.ghielmetti.utilities.swing.JNumberField;
import net.ghielmetti.utilities.swing.Scroller;

/**
 * A panel that displays the Pokémon values and allows the User to change them.
 *
 * @author Leopoldo Ghielmetti
 */
public class ValuesPanel extends JPanel implements Observer {
  private static final Logger    LOGGER            = LoggerFactory.getLogger(ValuesPanel.class);
  private JNumberField           cp;
  private JNumberField           hp;
  private JComboBox<Integer>     sd;
  private JComboBox<Limit>       code;
  private final ValuesModelIfc   model;

  private final FocusListener    sdFocusListener   = new FocusListener() {
                                                     @Override
                                                     public void focusGained(final FocusEvent inEvent) {
                                                       focusGainedSD();
                                                     }

                                                     @Override
                                                     public void focusLost(final FocusEvent inEvent) {
                                                       focusLostSD();
                                                     }
                                                   };

  private final FocusListener    codeFocusListener = new FocusListener() {
                                                     @Override
                                                     public void focusGained(final FocusEvent e) {
                                                       focusGainedCode();
                                                     }

                                                     @Override
                                                     public void focusLost(final FocusEvent e) {
                                                       focusLostCode();
                                                     }
                                                   };

  private final DocumentListener cpChangedListener = new DocumentListener() {
                                                     @Override
                                                     public void changedUpdate(final DocumentEvent e) {
                                                       actionUpdateCP();
                                                     }

                                                     @Override
                                                     public void insertUpdate(final DocumentEvent e) {
                                                       actionUpdateCP();
                                                     }

                                                     @Override
                                                     public void removeUpdate(final DocumentEvent e) {
                                                       actionUpdateCP();
                                                     }
                                                   };

  private final FocusListener    cpFocusListener   = new FocusAdapter() {
                                                     @Override
                                                     public void focusLost(final FocusEvent e) {
                                                       focusLostCP();
                                                     }
                                                   };

  private final DocumentListener hpChangedListener = new DocumentListener() {
                                                     @Override
                                                     public void changedUpdate(final DocumentEvent e) {
                                                       actionUpdateHP();
                                                     }

                                                     @Override
                                                     public void insertUpdate(final DocumentEvent e) {
                                                       actionUpdateHP();
                                                     }

                                                     @Override
                                                     public void removeUpdate(final DocumentEvent e) {
                                                       actionUpdateHP();
                                                     }
                                                   };

  private final FocusListener    hpFocusListener   = new FocusAdapter() {
                                                     @Override
                                                     public void focusLost(final FocusEvent e) {
                                                       focusLostHP();
                                                     }
                                                   };

  /**
   * Constructor.
   *
   * @param inModel The document model.
   */
  public ValuesPanel(final ValuesModelIfc inModel) {
    super(new GridBagLayout());

    model = inModel;
    model.addObserver(this);

    initialize();
  }

  /**
   * Returns the document model.
   *
   * @return The model.
   */
  public ValuesModelIfc getModel() {
    return model;
  }

  /** Restores the cursor on the CP text box. */
  @Override
  public boolean requestFocusInWindow() {
    super.requestFocusInWindow();
    return cp.requestFocusInWindow();
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    if (ValuesModelIfc.OBSERVE_LIMIT.equals(inArg)) {
      Limit limit = model.getLimit();

      if (!limit.equals(code.getSelectedItem())) {
        LOGGER.debug("Update Limit: {}", limit);
        code.setSelectedItem(limit);
      }
    }
  }

  private void initialize() {
    cp = new JNumberField(5);
    cp.getDocument().addDocumentListener(cpChangedListener);
    cp.addMouseWheelListener(new Scroller(Integer.valueOf(10), Integer.valueOf(99999)));
    cp.addFocusListener(cpFocusListener);
    cp.setText(model.getCP());

    hp = new JNumberField(5);
    hp.getDocument().addDocumentListener(hpChangedListener);
    hp.addMouseWheelListener(new Scroller(Integer.valueOf(10), Integer.valueOf(99999)));
    hp.addFocusListener(hpFocusListener);
    hp.setText(model.getHP());

    sd = new JComboBox<>(Multiplier.getInstance().getStardustList().toArray(new Integer[0]));
    sd.setEditable(true);
    sd.getEditor().getEditorComponent().addFocusListener(sdFocusListener);
    sd.addActionListener(e -> selectedSD());
    sd.addMouseWheelListener(new Scroller());
    sd.setSelectedIndex(0);

    code = new JComboBox<>(IVMap.getLimits().toArray(new Limit[0]));
    code.setEditable(true);
    code.getEditor().getEditorComponent().addFocusListener(codeFocusListener);
    code.addActionListener(e -> selectedCode());
    code.addMouseWheelListener(new Scroller());
    code.setSelectedIndex(0);

    add(new JLabel(Translations.translate("label.cp")), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(cp, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(new JLabel(Translations.translate("label.hp")), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(hp, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(new JLabel(Translations.translate("label.sd")), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(sd, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(new JLabel(Translations.translate("label.code")), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(code, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  private void selectedCode() {
    if (code.getSelectedItem() instanceof Limit) {
      model.setLimit((Limit) code.getSelectedItem());
    } else {
      model.setLimit((String) code.getSelectedItem());
    }
  }

  private void selectedSD() {
    model.setSD((Integer) sd.getSelectedItem());
  }

  /** Method called when the CP field is updated. */
  void actionUpdateCP() {
    model.setCP(Integer.valueOf(cp.getNumber().intValue()));
  }

  /** Method called when the HP field is updated. */
  void actionUpdateHP() {
    model.setHP(Integer.valueOf(hp.getNumber().intValue()));
  }

  /** Method called when the Code field gains focus. */
  void focusGainedCode() {
    code.getEditor().selectAll();
  }

  /** Method called when the SD field gains focus. */
  void focusGainedSD() {
    sd.getEditor().selectAll();
  }

  /** Method called when the Code field lose focus. */
  void focusLostCode() {
    Limit limit = new Limit(code.getSelectedItem().toString());
    code.setSelectedItem(limit);

    if (!limit.isValid()) {
      code.grabFocus();
    } else {
      model.setLimit(limit);
    }
  }

  /** Method called when the CP field lose focus. */
  void focusLostCP() {
    cp.setText(model.getCP());
  }

  /** Method called when the HP field lose focus. */
  void focusLostHP() {
    hp.setText(model.getHP());
  }

  /** Method called when the SD field lose focus. */
  void focusLostSD() {
    try {
      Integer item = Integer.valueOf(sd.getSelectedItem().toString());
      sd.setSelectedItem(item);

      if (sd.getSelectedIndex() == -1) {
        sd.grabFocus();
      } else {
        model.setSD((Integer) sd.getSelectedItem());
      }
    } catch (Exception e) {
      sd.grabFocus();
    }
  }
}
