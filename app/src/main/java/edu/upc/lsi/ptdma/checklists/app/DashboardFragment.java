package edu.upc.lsi.ptdma.checklists.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;


public class DashboardFragment extends BaseCardFragment {
  private NetworkHelper networkClient;
  private HashMap<String, Integer> dataValues;
  private Card.OnCardClickListener cardClickListener = new Card.OnCardClickListener() {
    @Override
    public void onClick(Card card, View view) {
      MainActivity activity = ((MainActivity)getActivity());

      switch(view.getId()){
        case R.id.dashboard_surveys_cardview:
          activity.switchMainFragment(SurveysFragment.newInstance("",""));
          break;
        case R.id.dashboard_incidences_cardview:
          activity.switchMainFragment(PlaceHolderFragment.newInstance(0));
          break;
        default:
          break;
      }
    }
  };

  public static DashboardFragment newInstance(NetworkHelper client) {
    DashboardFragment fragment = new DashboardFragment(client);
    return fragment;
  }

  public DashboardFragment(NetworkHelper client) {
    super();
    networkClient = client;
    client.getDashboardData(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dashboard, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    //addSectionsCard();
  }

  public void setDashboardDataValues(JSONObject data){

    if(dataValues == null)
      dataValues = new HashMap<String, Integer>();

    try{
      dataValues.put("surveys", data.getInt("surveys"));
      dataValues.put("incidences", data.getInt("incidences"));
      addSectionsCard();
    }
    catch(JSONException e) { throw new RuntimeException(e); }
  }

  private void addSectionsCard() {
    addSurveysToPerformCard();
    addIncidencesCard();
  }

  private void addSurveysToPerformCard() {
    addCardToCardView(
        R.id.dashboard_surveys_cardview,
        newDashboardCard("Surveys", dataValues.get("surveys")));
  }

  private void addIncidencesCard(){
    addCardToCardView(
        R.id.dashboard_incidences_cardview,
        newDashboardCard("Incidences", dataValues.get("incidences")));
  }

  private Card newDashboardCard(String s, Integer v){
    Card card = new Card(getActivity());
    card.addCardHeader(newDashboardCardHeaderFor(s, v));
    card.setOnClickListener(cardClickListener);
    return card;
  }

  private DashboardCardHeader newDashboardCardHeaderFor(String s, Integer v){
    return new DashboardCardHeader(getActivity(), s, v);
  }

}
