package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.lsi.ptdma.checklists.app.MainActivity;
import edu.upc.lsi.ptdma.checklists.app.network.NetworkHelper;
import edu.upc.lsi.ptdma.checklists.app.R;
import edu.upc.lsi.ptdma.checklists.app.models.SurveyCard;
import it.gmariotti.cardslib.library.internal.Card;

public class SurveysFragment extends ScrollableCardsViewFragment {
  public static SurveysFragment newInstance(NetworkHelper manager) {
    SurveysFragment fragment = new SurveysFragment(manager);
    return fragment;
  }

  public SurveysFragment(NetworkHelper manager) {
    super(manager);

    listViewId = R.id.surveys_card_list_view;

    listener = new Card.OnCardClickListener() {
      @Override
      public void onClick(Card card, View view) {
        MainActivity activity = ((MainActivity)getActivity());
        activity.switchMainFragment(
            SurveyFragment.newInstance(
                networkManager,
                ((SurveyCard)card).getSurveyId()));
      }
    };
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_surveys, container, false);
    networkManager.getSurveys(this);
    return view;
  }

  @Override
  protected ArrayList<Card> cardsList(JSONObject response) {
    ArrayList<Card> cards = new ArrayList<Card>();
    try {
      JSONArray surveys = (JSONArray) response.get("surveys");
      int len = surveys.length();

      for (int i = 0; i < len; i++){
        SurveyCard card = new SurveyCard(getActivity(), (JSONObject) surveys.get(i));
        card.setOnClickListener(listener);
        cards.add(card);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return cards;
  }
}
