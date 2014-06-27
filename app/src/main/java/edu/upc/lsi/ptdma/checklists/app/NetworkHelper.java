package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.content.Context;


import org.json.JSONObject;

import java.util.HashMap;

public class NetworkHelper implements GoogleAPIHelperListener{

  private Activity mainContext;
  private GoogleAPIHelper googleHelper;
  private CheckListsAPIHelper apiHelper;

  public NetworkHelper(Activity context){
    mainContext = context;
    googleHelper = new GoogleAPIHelper(mainContext, this);
    apiHelper = new CheckListsAPIHelper(this);
  }


  public void signIn(){ startSignInProcess(); }

  public void signOut(){
    googleHelper.disconnectClient();
  }



  private void startSignInProcess(){
    googleHelper.setSignInState(GoogleAPIHelper.STATE_SIGN_IN);
    googleHelper.connectClient();
  }

  public void onGoogleApiClientTokenReceived(HashMap credentials){
    HashMap userAuthHash = googleHelper.getUserAuthHash();
    userAuthHash.put("credentials", credentials);
    JSONObject json = new JSONObject(userAuthHash);

    //start the oauth sign in flow to my API
    apiHelper.setUserSignInRequestParams(json);
    apiHelper.startSignInFlow();

  }

  public Context applicationContext(){
    return mainContext;
  }

  public void onChecklistsAPIConnected(){
    ((NetworkEventsListener)mainContext).signedInToAPIs();
  }

  @Override
  public void onGoogleAPIConnected() {
    googleHelper.getAccessToken();
  }

  @Override
  public void onGoogleAPIDisconnected() { onGoogleAPISignedOut(); }

  @Override
  public void onGoogleAPIConnectionError() {}

  @Override
  public void onGoogleAPISignedOut() {
    ((MainActivity)mainContext).signedOutToAPIs();
  }


}
