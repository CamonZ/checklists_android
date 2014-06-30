package edu.upc.lsi.ptdma.checklists.app.network;

import android.app.Activity;
import android.content.Context;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import edu.upc.lsi.ptdma.checklists.app.fragments.DashboardFragment;
import edu.upc.lsi.ptdma.checklists.app.MainActivity;
import edu.upc.lsi.ptdma.checklists.app.fragments.SurveyFragment;
import edu.upc.lsi.ptdma.checklists.app.fragments.SurveysFragment;

public class NetworkHelper implements GoogleAPIHelperListener {

  private Activity mainContext;
  private GoogleAPIHelper googleApiClient;
  private CheckListsAPIHelper apiClient;
  private DashboardFragment dashboardViewController;
  private SurveysFragment surveysViewController;
  private SurveyFragment surveyViewController;

  public NetworkHelper(Activity context) {
    mainContext = context;
    googleApiClient = new GoogleAPIHelper(mainContext, this);
    apiClient = new CheckListsAPIHelper(this);
  }


  public void signIn() {
    startSignInProcess();
  }

  public void signOut() {
    googleApiClient.disconnectClient();
  }

  public void onGoogleApiClientTokenReceived(HashMap credentials) {
    HashMap userAuthHash = googleApiClient.getUserAuthHash();
    userAuthHash.put("credentials", credentials);
    JSONObject json = new JSONObject(userAuthHash);

    //start the oauth sign in flow to my API
    apiClient.setUserSignInRequestParams(json);
    apiClient.startSignInFlow();

  }

  public Context applicationContext() {
    return mainContext;
  }

  public void onChecklistsAPIConnected() {
    ((NetworkEventsListener) mainContext).signedInToAPIs();
  }

  @Override
  public void onGoogleAPIConnected() {
    googleApiClient.getAccessToken();
  }

  @Override
  public void onGoogleAPIDisconnected() {
    onGoogleAPISignedOut();
  }

  @Override
  public void onGoogleAPIConnectionError() {
  }

  @Override
  public void onGoogleAPISignedOut() {
    ((MainActivity) mainContext).signedOutToAPIs();
  }

  public void getDashboardData(DashboardFragment fragment){
    if(dashboardViewController == null) dashboardViewController = fragment;
    apiClient.getDashboardData();
  }

  public void onDashboardDataReceived(JSONObject object){
    dashboardViewController.setDashboardDataValues(object);
  }

  private void startSignInProcess() {
    googleApiClient.setSignInState(GoogleAPIHelper.STATE_SIGN_IN);
    googleApiClient.connectClient();
  }


  public void getSurveys(SurveysFragment fragment) {
    if(surveysViewController == null) surveysViewController = fragment;
    apiClient.getChecklists();
  }

  public void onChecklistsReceived(JSONArray response) {
    surveysViewController.populateListView(response);
  }

  public void getSurveyForId(SurveyFragment fragment, int id) {
    if(surveyViewController == null) surveyViewController = fragment;
    apiClient.getChecklist(id);
  }

  public void onChecklistReceived(JSONObject response) {
    surveyViewController.populateListView(response);
  }

  public void saveSurvey(JSONObject data) {
    apiClient.postSurvey(data);
  }

  public void onSurveySent() {
    ((MainActivity)mainContext).switchMainFragment(dashboardViewController);

    //make a toast with the success
  }
}
