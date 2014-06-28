package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

public abstract class BaseCardFragment extends Fragment {
  private CardInteractionsListener listener;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      //listener = (CardInteractionsListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement CardInteractionsListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }


  protected void addCardToCardView(int id, Card card) {
    CardView cardView = (CardView) getActivity().findViewById(id);
    cardView.setCard(card);
  }


}