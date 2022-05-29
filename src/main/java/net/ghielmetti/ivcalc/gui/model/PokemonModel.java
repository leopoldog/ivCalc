package net.ghielmetti.ivcalc.gui.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import net.ghielmetti.ivcalc.data.CandidateDetail;
import net.ghielmetti.ivcalc.data.CandidateList;
import net.ghielmetti.ivcalc.data.IVLevel;
import net.ghielmetti.ivcalc.data.Limit;
import net.ghielmetti.ivcalc.data.Pokemon;
import net.ghielmetti.ivcalc.database.PokemonDatabase;
import net.ghielmetti.ivcalc.pokedex.Pokedex;
import net.ghielmetti.ivcalc.pokedex.Team;
import net.ghielmetti.ivcalc.tools.PreferenceStore;
import net.ghielmetti.utilities.VersionReader;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The model for the Pokémon application.
 *
 * @author Leopoldo Ghielmetti
 */
public class PokemonModel extends Observable implements PokemonModelIfc {
  private static final Logger LOGGER         = LoggerFactory.getLogger(PokemonModel.class);
  private static final Logger OUTPUT         = LoggerFactory.getLogger("Output");
  private final String        version        = VersionReader.getVersion(ImmutablePair.of("net.ghielmetti", "ivcalc"));
  private Pokedex             pokedex;
  private Team                team;
  private Limit               limit;
  private Integer             cp             = 10;
  private Integer             hp             = 10;
  private Integer             sd             = 200;
  private List<Pokemon>       pokemons       = new ArrayList<>();
  private boolean             showHalfLevels = true;
  private CandidateList       selectedCandidate;
  private List<CandidateList> candidates     = new ArrayList<>();
  private CandidateDetail     selectedDetail;
  // TODO try do do otherwise, I don't like this model in model way of life.
  private GoodnessCheckerModelIfc goodness;

  /**
   * Creates a new model.
   *
   * @param inTeam The {@link Team} of the user.
   * @param inPokedex The {@link Pokedex}.
   * @param inGoodness The {@link GoodnessModelIfc} to check if a Pokémon is good or not.
   */
  public PokemonModel(final Team inTeam, final Pokedex inPokedex, final GoodnessCheckerModelIfc inGoodness) {
    team = inTeam;
    pokedex = inPokedex;
    goodness = inGoodness;
  }

  @Override
  public Collection<Pokemon> getAllPokemons() {
    return pokedex.getAllPokemons();
  }

  @Override
  public List<CandidateList> getCandidates() {
    return candidates;
  }

  @Override
  public Integer getCP() {
    return cp;
  }

  /**
   * Returns the GoodnessCheckerModelIfc.
   *
   * @return The {@link GoodnessCheckerModelIfc}.
   */
  public GoodnessCheckerModelIfc getGoodnessModel() {
    return goodness;
  }

  @Override
  public Integer getHP() {
    return hp;
  }

  @Override
  public Limit getLimit() {
    return limit;
  }

  /**
   * Accessor.
   *
   * @return The Pokédex.
   */
  public Pokedex getPokedex() {
    return pokedex;
  }

  @Override
  public List<Pokemon> getPokemons() {
    return Collections.unmodifiableList(pokemons);
  }

  @Override
  public CandidateList getSavedCandidates(final Pokemon inPokemon, final String inName, final Limit inLimit) {
    CandidateList candidate = PokemonDatabase.getInstance().getSavedCandidates(inPokemon, inName, inLimit);

    if (!candidate.isEmpty()) {
      return candidate;
    }

    for (Pokemon pokemon : pokedex.getAncestors(inPokemon.getName())) {
      candidate = PokemonDatabase.getInstance().getSavedCandidates(pokemon, inName, inLimit);

      if (!candidate.isEmpty()) {
        return candidate;
      }
    }

    return null;
  }

  @Override
  public List<String> getSavedCandidatesNames(final Pokemon inPokemon, final Limit inLimit) {
    List<String> names = new ArrayList<>(PokemonDatabase.getInstance().listSavedCandidates(inPokemon, inLimit));

    for (Pokemon pokemon : pokedex.getAncestors(inPokemon.getName())) {
      names.addAll(PokemonDatabase.getInstance().listSavedCandidates(pokemon, inLimit));
    }

    Collections.sort(names);

    return names;
  }

  /**
   * Accessor.
   *
   * @return The value for SD.
   */
  public Integer getSD() {
    return sd;
  }

  @Override
  public CandidateList getSelectedCandidate() {
    return selectedCandidate;
  }

  @Override
  public CandidateDetail getSelectedDetail() {
    return selectedDetail;
  }

  @Override
  public boolean getShowHalfLevels() {
    return showHalfLevels;
  }

  @Override
  public String getStatsLabel(final int inStatNumber) {
    return team.getStatsLabel(inStatNumber);
  }

  /**
   * Accessor.
   *
   * @return The {@link Team}.
   */
  public Team getTeam() {
    return team;
  }

  @Override
  public String getTotalLabel(final int inTotalNumber) {
    return team.getTotalLabel(inTotalNumber);
  }

  /**
   * Accessor.
   *
   * @return The version of the software.
   */
  public String getVersion() {
    return version;
  }

  @Override
  public Rectangle getViewBounds() {
    return PreferenceStore.getViewBounds();
  }

  @Override
  public int getViewExtendedState() {
    return PreferenceStore.getViewExtendedState();
  }

  @Override
  public boolean isGood(final String inPokemonName, final IVLevel inIVLevel) {
    return goodness.isGood(inPokemonName, inIVLevel);
  }

  @Override
  public void removeSavedCandidates(final Pokemon inPokemon, final String inName) {
    PokemonDatabase.getInstance().removeSavedCandidates(inPokemon, inName);

    for (Pokemon pokemon : pokedex.getAncestors(inPokemon.getName())) {
      PokemonDatabase.getInstance().removeSavedCandidates(pokemon, inName);
    }
  }

  @Override
  public void saveCandidate(final CandidateList inCandidates, final String inName) {
    if (inName != null) {
      PokemonDatabase.getInstance().removeSavedCandidates(inCandidates.getPokemon(), inName);
      PokemonDatabase.getInstance().saveCandidates(inCandidates, inName);
    }
  }

  /** Compute the candidates based on the model configuration and notifies the observers. */
  public void searchCandidates() {
    candidates = searchCandidates(pokemons, limit, cp, hp, sd);
    LOGGER.debug("New candidates list");
    setChanged();
    notifyObservers(OBSERVE_CANDIDATE_LIST);
  }

  /**
   * Compute the candidates using the given parameters.
   *
   * @param inPokemons The list of Pokémons to scan.
   * @param inLimit The Limit.
   * @param inCP The CP value.
   * @param inHP The HP value.
   * @param inSD The SD value.
   * @return A list of matching candidates.
   */
  private List<CandidateList> searchCandidates(final List<Pokemon> inPokemons, final Limit inLimit, final int inCP, final int inHP, final int inSD) {
    List<CandidateList> newList = new ArrayList<>();

    for (Pokemon pokemon : inPokemons) {
      CandidateList candidateList = pokemon.getIV(inLimit, inCP, inHP, inSD);

      if (candidateList.size() > 0) {
        newList.add(candidateList);
        OUTPUT.info("{}", candidateList);
      }
    }

    return newList;
  }

  @Override
  public void setBestQualityAttack(final boolean inBestQualityAttack) {
    if (limit.isAttack() != inBestQualityAttack) {
      setLimit(new Limit(limit.getLevel(), limit.getStrength(), limit.isHP(), inBestQualityAttack, limit.isDefense()));
    }
  }

  @Override
  public void setBestQualityDefense(final boolean inBestQualityDefense) {
    if (limit.isDefense() != inBestQualityDefense) {
      setLimit(new Limit(limit.getLevel(), limit.getStrength(), limit.isHP(), limit.isAttack(), inBestQualityDefense));
    }
  }

  @Override
  public void setBestQualityHP(final boolean inBestQualityHP) {
    if (limit.isHP() != inBestQualityHP) {
      setLimit(new Limit(limit.getLevel(), limit.getStrength(), inBestQualityHP, limit.isAttack(), limit.isDefense()));
    }
  }

  @Override
  public void setCP(final Integer inCP) {
    Integer value = inCP == null || inCP.intValue() < 10 ? 10 : inCP;
    if (!value.equals(cp)) {
      cp = value;
      LOGGER.debug("New CP: {}", inCP);
      setChanged();
      notifyObservers(OBSERVE_CP);
    }
  }

  @Override
  public void setHP(final Integer inHP) {
    Integer value = inHP == null || inHP.intValue() < 10 ? 10 : inHP;
    if (!value.equals(hp)) {
      hp = value;
      LOGGER.debug("New HP: {}", inHP);
      setChanged();
      notifyObservers(OBSERVE_HP);
    }
  }

  @Override
  public void setLimit(final Limit inLimit) {
    if (!Objects.equals(inLimit, limit)) {
      limit = inLimit;
      LOGGER.debug("New limit: {}", limit);
      setChanged();
      notifyObservers(OBSERVE_LIMIT);
    }
  }

  @Override
  public void setLimit(final String inCode) {
    setLimit(new Limit(inCode));
  }

  @Override
  public void setPokemons(final List<Pokemon> inPokemonList) {
    if ((!pokemons.containsAll(inPokemonList) || !inPokemonList.containsAll(pokemons))) {
      pokemons = new ArrayList<>(inPokemonList);
      LOGGER.debug("New pokemon list: {}", pokemons);
      setChanged();
      notifyObservers(OBSERVE_POKEMONS);
    }
  }

  @Override
  public void setSD(final Integer inSD) {
    if (!Objects.equals(inSD, sd)) {
      sd = inSD;
      LOGGER.debug("New SD: {}", inSD);
      setChanged();
      notifyObservers(OBSERVE_SD);
    }
  }

  @Override
  public void setSelectedCandidate(final CandidateList inSelectedCandidates) {
    if (!Objects.equals(selectedCandidate, inSelectedCandidates)) {
      selectedCandidate = inSelectedCandidates;
      LOGGER.debug("New selected candidate: {}", inSelectedCandidates);
      setChanged();
      notifyObservers(OBSERVE_SELECTED_CANDIDATE);
    }
  }

  @Override
  public void setSelectedDetails(final CandidateDetail inSelectedDetail) {
    if (!Objects.equals(selectedDetail, inSelectedDetail)) {
      selectedDetail = inSelectedDetail;
      LOGGER.debug("New selected detail: {}", inSelectedDetail);
      setChanged();
      notifyObservers(OBSERVE_SELECTED_DETAIL);
    }
  }

  @Override
  public void setShowHalfLevels(final boolean inShow) {
    if (showHalfLevels != inShow) {
      showHalfLevels = inShow;
      LOGGER.debug("New show half levels: {}", Boolean.valueOf(inShow));
      setChanged();
      notifyObservers(OBSERVE_HALF_LEVELS);
    }
  }

  @Override
  public void setStats(final int inStats) {
    if (limit.getStrength() != inStats) {
      setLimit(new Limit(limit.getLevel(), inStats, limit.isHP(), limit.isAttack(), limit.isDefense()));
    }
  }

  /**
   * Sets the team for the user.
   *
   * @param inTeam The new {@link Team}.
   */
  public void setTeam(final Team inTeam) {
    if (team != inTeam) {
      team = inTeam;
      LOGGER.debug("New team: {}", team);
      setChanged();
      notifyObservers(OBSERVE_TEAM);
    }
  }

  @Override
  public void setTotal(final int inTotal) {
    if (limit.getLevel() != inTotal) {
      setLimit(new Limit(inTotal, limit.getStrength(), limit.isHP(), limit.isAttack(), limit.isDefense()));
    }
  }

  @Override
  public void setViewBounds(final Rectangle inBounds) {
    PreferenceStore.setViewBounds(inBounds);
  }

  @Override
  public void setViewExtendedState(final int inExtendedState) {
    PreferenceStore.setViewExtendedState(inExtendedState);
  }
}
