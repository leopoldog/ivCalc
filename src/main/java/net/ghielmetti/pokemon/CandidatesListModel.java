package net.ghielmetti.pokemon;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class CandidatesListModel implements ListModel<CandidatePanel> {
  private List<CandidatePanel>   list      = new ArrayList<>();
  private List<ListDataListener> listeners = new ArrayList<>();

  public void add(final CandidatePanel inCandidate) {
    list.add(inCandidate);
    for (ListDataListener listener : listeners) {
      listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
    }
  }

  @Override
  public void addListDataListener(final ListDataListener inListener) {
    listeners.add(inListener);
  }

  public void clear() {
    list.clear();
    for (ListDataListener listener : listeners) {
      listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
    }
  }

  @Override
  public CandidatePanel getElementAt(final int index) {
    return list.get(index);
  }

  @Override
  public int getSize() {
    return list.size();
  }

  public void remove(final CandidatePanel inCandidate) {
    list.remove(inCandidate);
    for (ListDataListener listener : listeners) {
      listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
    }
  }

  @Override
  public void removeListDataListener(final ListDataListener inListener) {
    listeners.remove(inListener);
  }
}
