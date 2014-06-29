package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;


import org.json.JSONObject;

import java.util.HashMap;

public class NetworkHelper implements GoogleAPIHelperListener {

  private Activity mainContext;
  private GoogleAPIHelper googleApiClient;
  private CheckListsAPIHelper apiClient;
  private DashboardFragment dashboardViewController;

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

  public void getDashboardData(DashboardFragment f){
    if(dashboardViewController == null) dashboardViewController = f;
    apiClient.getDashboardData();
  }

  public void onDashboardDataReceived(JSONObject object){
    dashboardViewController.setDashboardDataValues(object);
  }

  private void startSignInProcess() {
    googleApiClient.setSignInState(GoogleAPIHelper.STATE_SIGN_IN);
    googleApiClient.connectClient();
  }


}
