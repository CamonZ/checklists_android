package edu.upc.lsi.ptdma.checklists.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;


public class DashboardFragment extends BaseCardFragment {
  private NetworkHelper networkClient;
  private HashMap<String, Integer> dataValues;

  public static DashboardFragment newInstance(NetworkHelper client, HashMap<String, Integer> data) {
    DashboardFragment fragment = new DashboardFragment(client, data);
    return fragment;
  }

  public DashboardFragment(NetworkHelper client, HashMap<String, Integer> data) {
    super();
    networkClient = client;
    dataValues = data;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dashboard, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    addSectionsCard();
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
    return card;
  }

  private DashboardCardHeader newDashboardCardHeaderFor(String s, Integer v){
    return new DashboardCardHeader(getActivity(), s, v);
  }

}
