package net.ghielmetti.ivcalc.gui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.gui.model.PokemonChooserModelIfc;
import net.ghielmetti.utilities.Translations;

/**
 * This panel allow the selection of Pokémons from a list.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonChooserPanel extends JPanel implements Observer {
  private JScrollPane                  scrollPokemonsList;
  private JTextField                   pokemonName;
  private JList<Pokemon>               selectedPokemons;
  private final PokemonChooserModelIfc model;

  private KeyListener                  pokemonNameKeyListener   = new KeyAdapter() {
                                                                  @Override
                                                                  public void keyTyped(final KeyEvent e) {
                                                                    keyTypedAction();
                                                                  }
                                                                };

  private FocusListener                pokemonNameFocusListener = new FocusListener() {
                                                                  @Override
                                                                  public void focusGained(final FocusEvent e) {
                                                                    focusGainedAction();
                                                                  }

                                                                  @Override
                                                                  public void focusLost(final FocusEvent e) {
                                                                    focusLostAction();
                                                                  }
                                                                };

  /**
   * Constructor.
   *
   * @param inModel The model.
   */
  public PokemonChooserPanel(final PokemonChooserModelIfc inModel) {
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
  public PokemonChooserModelIfc getModel() {
    return model;
  }

  @Override
  public boolean requestFocusInWindow() {
    if (!pokemonName.getText().isEmpty()) {
      return pokemonName.requestFocusInWindow();
    }

    return false;
  }

  /**
   * Defines the name field height.
   *
   * @param inHeight The height.
   */
  public void setNameFieldHeight(final int inHeight) {
    pokemonName.setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), inHeight));
    pokemonName.setMinimumSize(pokemonName.getPreferredSize());
  }

  @Override
  public void update(final Observable inObservable, final Object inArg) {
    // nothing to do, this list is not to be updated
  }

  private void initialize() {
    pokemonName = new JTextField();
    pokemonName.addKeyListener(pokemonNameKeyListener);
    pokemonName.addFocusListener(pokemonNameFocusListener);

    Font font = pokemonName.getFont();
    StringBuilder style = new StringBuilder("font-family:" + font.getFamily() + ";");
    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
    style.append("font-size:" + font.getSize() + "pt;");
    style.append("margin-bottom: 0.2cm;");
    style.append("line-height: 100%;");

    Pokemon[] pokemons = model.getAllPokemons().toArray(new Pokemon[0]);

    Arrays.sort(pokemons);

    selectedPokemons = new JList<>(pokemons);
    selectedPokemons.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    selectedPokemons.addListSelectionListener(this::selectNames);
    selectedPokemons.setCellRenderer(new DefaultListCellRenderer() {
      private String TABLE_START = "<table width=\"120\" cellpadding=\"4\" cellspacing=\"0\"><col width=\"70\"><col width=\"30\">";
      private String TABLE_END   = "</table>";

      @Override
      public Component getListCellRendererComponent(final JList<?> inList, final Object inValue, final int inIndex, final boolean inIsSelected, final boolean inCellHasFocus) {
        Pokemon pokemon = (Pokemon) inValue;
        JLabel label = (JLabel) super.getListCellRendererComponent(inList, pokemon, inIndex, inIsSelected, inCellHasFocus);
        label.setIcon(pokemon.getSmallIcon());
        ImmutableTriple<Integer, Integer, Integer> baseValues = pokemon.getBaseValues();
        ImmutablePair<Integer, Integer> minMax40 = pokemon.getMinMaxCP(80);
        label.setToolTipText( //
            "<html><body><p style=\"" + style + "\"><b>" + pokemon.getName() + ":</b></p>" //
                + "<dl><dt>" + Translations.translate("label.base") + "</dt><dd>" //
                + TABLE_START//
                + getRow(Translations.translate("label.attack"), baseValues.left) //
                + getRow(Translations.translate("label.defense"), baseValues.middle) //
                + getRow(Translations.translate("label.stamina"), baseValues.right) //
                + TABLE_END //
                + "</dd><dd></dd><dt>" + Translations.translate("label.at40") + "</dt><dd>" //
                + TABLE_START //
                + getRow(Translations.translate("label.minCP"), minMax40.left) //
                + getRow(Translations.translate("label.maxCP"), minMax40.right) //
                + TABLE_END //
                + "</dd></dl></body></html>");
        return label;
      }

      private String getRow(final String inLabel, final Integer inValue) {
        return "<tr valign=\"top\"><td width=\"70\" style=\"border: none; padding: 0cm\"><p>" + inLabel + ":</p></td>" //
            + "<td width=\"30\" style=\"border: none; padding: 0cm\"><p align=\"right\">" + inValue + "</p></td></tr>";
      }
    });

    scrollPokemonsList = new JScrollPane(selectedPokemons);

    add(new JLabel(Translations.translate("label.name")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(pokemonName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    add(scrollPokemonsList, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    // Force the update of the model
    keyTypedAction();
  }

  private void selectNames(final ListSelectionEvent inEvent) {
    if (!inEvent.getValueIsAdjusting()) {
      @SuppressWarnings("unchecked")
      JList<Pokemon> list = (JList<Pokemon>) inEvent.getSource();
      model.setPokemons(list.getSelectedValuesList());
    }
  }

  /** Method called when the focus is gained. */
  void focusGainedAction() {
    pokemonName.selectAll();
  }

  /** Method called when the focus is lost. */
  void focusLostAction() {
    if (selectedPokemons.isSelectionEmpty()) {
      pokemonName.grabFocus();
    }
  }

  /** Method called when a key is typed in the Pokémon name field. */
  void keyTypedAction() {
    SwingUtilities.invokeLater(() -> {
      String search = pokemonName.getText().trim().toLowerCase();
      List<Integer> indices = new ArrayList<>();
      selectedPokemons.setValueIsAdjusting(true);
      selectedPokemons.clearSelection();

      for (int index = selectedPokemons.getModel().getSize() - 1; index >= 0; index--) {
        if (selectedPokemons.getModel().getElementAt(index).getName().toLowerCase().indexOf(search) != -1) {
          indices.add(Integer.valueOf(index));
        }
      }

      selectedPokemons.setSelectedIndices(indices.stream().mapToInt(Integer::intValue).toArray());

      if (!indices.isEmpty()) {
        selectedPokemons.ensureIndexIsVisible(indices.get(indices.size() - 1).intValue());
      }

      selectedPokemons.setValueIsAdjusting(false);
    });
  }
}
