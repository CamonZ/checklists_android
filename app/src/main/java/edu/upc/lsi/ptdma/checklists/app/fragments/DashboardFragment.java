package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import edu.upc.lsi.ptdma.checklists.app.MainActivity;
import edu.upc.lsi.ptdma.checklists.app.R;
import edu.upc.lsi.ptdma.checklists.app.models.DashboardCardHeader;
import edu.upc.lsi.ptdma.checklists.app.network.NetworkHelper;
import it.gmariotti.cardslib.library.internal.Card;


public class DashboardFragment extends CardsViewFragment {
  private HashMap<String, Integer> dataValues;

  public static DashboardFragment newInstance(NetworkHelper manager) {
    DashboardFragment fragment = new DashboardFragment(manager);
    return fragment;
  }

  public DashboardFragment(NetworkHelper manager) {
    super(manager);

    listener = new Card.OnCardClickListener() {
      @Override
      public void onClick(Card card, View view) {
        MainActivity activity = ((MainActivity)getActivity());
        switch(view.getId()){
          case R.id.dashboard_surveys_cardview:
            activity.switchMainFragment(SurveysFragment.newInstance(networkManager));
            break;
          case R.id.dashboard_incidences_cardview:
            activity.switchMainFragment(PlaceHolderFragment.newInstance(0));
            break;
          default:
            break;
        }
      }
    };

    manager.getDashboardData(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dashboard, container, false);
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
    card.setOnClickListener(listener);
    return card;
  }

  private DashboardCardHeader newDashboardCardHeaderFor(String s, Integer v){
    return new DashboardCardHeader(getActivity(), s, v);
  }

}
