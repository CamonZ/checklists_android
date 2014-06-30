package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.lsi.ptdma.checklists.app.R;
import edu.upc.lsi.ptdma.checklists.app.models.SurveyCard;
import edu.upc.lsi.ptdma.checklists.app.models.SurveyQuestionCard;
import edu.upc.lsi.ptdma.checklists.app.network.NetworkHelper;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class SurveyFragment extends ScrollableCardsViewFragment{

  private int id;

  public static SurveyFragment newInstance(NetworkHelper manager, int surveyId) {
    return new SurveyFragment(manager, surveyId);
  }

  public SurveyFragment(NetworkHelper manager, int surveyId) {
    super(manager);
    listViewId = R.id.survey_card_list_view;
    id = surveyId;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_survey, container, false);
    networkManager.getSurveyForId(this, id);
    return view;
  }

  @Override
  protected ArrayList<Card> cardsList(JSONObject response) {
    ArrayList<Card> cards = new ArrayList<Card>();
    try {
      JSONArray surveys = (JSONArray) response.get("survey");
      int len = surveys.length();

      for (int i = 0; i < len; i++)
        cards.add(
            new SurveyQuestionCard(
                getActivity(),
                (JSONObject) surveys.get(i), i + 1)
        );


    } catch (JSONException e) {
      e.printStackTrace();
    }
    return cards;
  }
}

