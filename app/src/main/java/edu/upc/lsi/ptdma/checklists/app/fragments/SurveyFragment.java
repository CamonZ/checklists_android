package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.lsi.ptdma.checklists.app.R;
import edu.upc.lsi.ptdma.checklists.app.models.SurveyQuestionCard;
import edu.upc.lsi.ptdma.checklists.app.network.NetworkHelper;
import it.gmariotti.cardslib.library.internal.Card;

public class SurveyFragment extends ScrollableCardsViewFragment implements
    View.OnClickListener{

  private int id;
  private String name;
  private String description;
  private ArrayList<Card> cards;
  private Button saveButton;

  public static SurveyFragment newInstance(NetworkHelper manager, int checklistId) {
    return new SurveyFragment(manager, checklistId);
  }

  public SurveyFragment(NetworkHelper manager, int checklistId) {
    super(manager);
    listViewId = R.id.survey_card_list_view;
    id = checklistId;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_survey, container, false);

    saveButton = (Button) view.findViewById(R.id.survey_save_button);
    saveButton.setOnClickListener(this);

    networkManager.getSurveyForId(this, id);
    return view;
  }

  @Override
  protected ArrayList<Card> cardsList(JSONObject response) {
    cards = new ArrayList<Card>();
    try {
      JSONObject survey = (JSONObject)response.get("checklist");
      JSONArray surveyQuestions = (JSONArray) survey.get("check_items");

      int len = surveyQuestions.length();

      for (int i = 0; i < len; i++)
        cards.add(
            new SurveyQuestionCard(
                getActivity(),
                (JSONObject) surveyQuestions.get(i), i + 1)
        );


    } catch (JSONException e) {
      e.printStackTrace();
    }
    return cards;
  }

  public JSONObject toJSONObject(){
    JSONObject json = new JSONObject();
    JSONArray cardsJSONArray = new JSONArray();
    try {
      for (Card card : cards)
        cardsJSONArray.put(((SurveyQuestionCard) card).toJSONObject());
      json.put("check_item_results", cardsJSONArray);
      json.put("checklist_id", id);
    }
    catch (JSONException e) { e.printStackTrace(); }
    return json;
  }

  @Override
  public void onClick(View view) {
    networkManager.saveSurvey(this.toJSONObject());
  }
}


