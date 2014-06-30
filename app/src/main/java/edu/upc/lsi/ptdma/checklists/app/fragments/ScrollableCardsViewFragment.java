package edu.upc.lsi.ptdma.checklists.app.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.lsi.ptdma.checklists.app.network.NetworkHelper;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public abstract class ScrollableCardsViewFragment extends CardsViewFragment {
  protected int listViewId;
  protected CardListView listView;
  protected CardArrayAdapter listAdapter;

  public ScrollableCardsViewFragment(NetworkHelper manager) {
    super(manager);
  }


  public void populateListView(JSONObject response) {
    if(listView == null)
      listView = (CardListView) getActivity().findViewById(listViewId);

    setListAdapter(cardsList(response));
  }

  public void populateListView(JSONArray response) {
    if(listView == null)
      listView = (CardListView) getActivity().findViewById(listViewId);

    setListAdapter(cardsList(response));
  }

  protected ArrayList<Card> cardsList(JSONObject response){
    throw new UnsupportedOperationException("Must implement this method on the subclass");
  }

  protected ArrayList<Card> cardsList(JSONArray response){
    throw new UnsupportedOperationException("Must implement this method on the subclass");
  }

  protected void setListAdapter(ArrayList<Card> cards){
    if(listAdapter == null) listAdapter = new CardArrayAdapter(getActivity(), cards);
    else{
      listAdapter.clear();
      listAdapter.addAll(cards);
    }

    listView.setAdapter(listAdapter);
  }
}
