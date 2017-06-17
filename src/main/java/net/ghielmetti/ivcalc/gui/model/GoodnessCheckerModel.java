package net.ghielmetti.ivcalc.gui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.ghielmetti.ivcalc.data.GoodnessChecker;
import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.database.PokemonDatabase;
import net.ghielmetti.ivcalc.pokedex.Pokedex;
import net.ghielmetti.utilities.Translations;

/**
 * A {@link TableModel} that manages the {@link GoodnessChecker} list.
 *
 * @author Leopoldo Ghielmetti
 */
public class GoodnessCheckerModel implements GoodnessCheckerModelIfc {
  /** The name of the global {@link GoodnessChecker}. */
  private static final GoodnessChecker       GLOBAL_CHECKER  = new GoodnessChecker("_GLOBAL_", 30, 10, 10, 10);
  private Pokedex                            pokedex;
  private List<TableModelListener>           listeners       = new ArrayList<>();
  private List<String>                       deletedCheckers = new ArrayList<>();
  private SortedMap<String, GoodnessChecker> checkers        = new TreeMap<>();
  private List<String>                       checkersNames   = new ArrayList<>();

  /**
   * Constructor.
   *
   * @param inPokedex The {@link Pokedex}.
   */
  public GoodnessCheckerModel(final Pokedex inPokedex) {
    pokedex = inPokedex;
    initializeGoodnessChecker();
  }

  @Override
  public void addTableModelListener(final TableModelListener inListener) {
    listeners.add(inListener);
  }

  @Override
  public void createChecker(final String inName) {
    checkersNames.add(inName);
    deletedCheckers.remove(inName);
    checkers.put(inName, new GoodnessChecker(inName, checkers.get(GLOBAL_CHECKER.getName())));
    Collections.sort(checkersNames);

    for (TableModelListener listener : listeners) {
      listener.tableChanged(new TableModelEvent(this, checkersNames.size() - 1, checkersNames.size() - 1, -1));
    }
  }

  @Override
  public void deleteChecker(final int inRowIndex) {
    if (inRowIndex >= 0 && inRowIndex < checkersNames.size()) {
      String name = checkersNames.remove(inRowIndex);
      deletedCheckers.add(name);
      checkers.remove(name);

      for (TableModelListener listener : listeners) {
        listener.tableChanged(new TableModelEvent(this, 0, checkersNames.size() - 1, -1));
      }
    }
  }

  @Override
  public List<String> getAllAvailableNames() {
    List<String> names = new ArrayList<>(pokedex.getAllNames());

    for (String name : checkersNames) {
      names.remove(name);
    }

    return names;
  }

  @Override
  public Class<?> getColumnClass(final int inColumnIndex) {
    switch (inColumnIndex) {
      case 0:
      case 6:
        return String.class;
      default:
        return Integer.class;
    }
  }

  @Override
  public int getColumnCount() {
    return 5;
  }

  @Override
  public String getColumnName(final int inColumnIndex) {
    switch (inColumnIndex) {
      case 0:
        return Translations.translate("label.name");
      case 1:
        return Translations.translate("label.total");
      case 2:
        return Translations.translate("label.attack");
      case 3:
        return Translations.translate("label.defense");
      case 4:
        return Translations.translate("label.stamina");
      default:
        return "";
    }
  }

  /**
   * Returns a list of all the Pokémons that have an associated {@link GoodnessChecker}.
   *
   * @return A list of {@link String}.
   */
  public Collection<String> getPokemonsWithCheckers() {
    return Collections.unmodifiableCollection(checkersNames);
  }

  @Override
  public int getRowCount() {
    return checkers.size();
  }

  @Override
  public Object getValueAt(final int inRowIndex, final int inColumnIndex) {
    String name = checkersNames.get(inRowIndex);
    switch (inColumnIndex) {
      case 0:
        return name;
      case 1:
        return Integer.valueOf(checkers.get(name).getMinTotal());
      case 2:
        return Integer.valueOf(checkers.get(name).getMinAttack());
      case 3:
        return Integer.valueOf(checkers.get(name).getMinDefense());
      case 4:
        return Integer.valueOf(checkers.get(name).getMinStamina());
      default:
        return "";
    }
  }

  @Override
  public boolean isCellEditable(final int inRowIndex, final int inColumnIndex) {
    return inColumnIndex > 0;
  }

  /**
   * Checks if this {@link IVLevel} is to be considered good for this Pokémon.<br>
   * If no goodness checker is defined, it's always good.
   *
   * @param inPokemonName The name of the Pokémon to check.
   * @param inIVLevel The {@link IVLevel} to check.
   * @return <code>true</code> if it's good.
   */
  @Override
  public boolean isGood(final String inPokemonName, final IVLevel inIVLevel) {
    GoodnessChecker gc = checkers.get(inPokemonName);

    // No specific goodness checker, check using the global one.
    if (gc == null) {
      return isGood(GLOBAL_CHECKER.getName(), inIVLevel);
    }

    return gc.isGood(inIVLevel);
  }

  @Override
  public void removeTableModelListener(final TableModelListener inListener) {
    listeners.remove(inListener);
  }

  @Override
  public void save() {
    for (String name : deletedCheckers) {
      PokemonDatabase.getInstance().removeGoodnessChecker(name);
    }

    for (GoodnessChecker checker : checkers.values()) {
      PokemonDatabase.getInstance().saveGoodnessChecker(checker);
    }
  }

  @Override
  public void setValueAt(final Object inValue, final int inRowIndex, final int inColumnIndex) {
    GoodnessChecker checker = checkers.get(checkersNames.get(inRowIndex));

    switch (inColumnIndex) {
      case 1:
        checker.setMinTotal(Integer.parseInt(inValue.toString()));
        break;
      case 2:
        checker.setMinAttack(Integer.parseInt(inValue.toString()));
        break;
      case 3:
        checker.setMinDefense(Integer.parseInt(inValue.toString()));
        break;
      case 4:
        checker.setMinStamina(Integer.parseInt(inValue.toString()));
        break;
      default:
        break;
    }
  }

  private void initializeGoodnessChecker() {
    for (GoodnessChecker gc : PokemonDatabase.getInstance().listGoodnessCheckers()) {
      checkers.put(gc.getName(), gc);
      checkersNames.add(gc.getName());
    }

    if (!checkersNames.contains(GLOBAL_CHECKER.getName())) {
      checkers.put(GLOBAL_CHECKER.getName(), GLOBAL_CHECKER);
      checkersNames.add(GLOBAL_CHECKER.getName());
    }

    Collections.sort(checkersNames);
  }
}
