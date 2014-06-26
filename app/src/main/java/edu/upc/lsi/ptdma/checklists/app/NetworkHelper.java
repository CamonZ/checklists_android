package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;

public class NetworkHelper implements GoogleAPIHelperListener{

  private Activity mainContext;
  private GoogleAPIHelper googleHelper;


  public NetworkHelper(Activity context){
    mainContext = context;
    googleHelper = new GoogleAPIHelper(mainContext, this);
  }


  public void signIn(){ startSignInProcess(); }

  public void signOut(){}



  private void startSignInProcess(){
    googleHelper.setSignInState(GoogleAPIHelper.STATE_SIGN_IN);
    googleHelper.connectClient();
  }

  public void clientTokenReceived(String accessToken){
    //YOLO 360 NOSCOPE LOL
  }

  private void finishSignIn(){

  }

  @Override
  public void onGoogleAPIConnected() {
    googleHelper.getAccessTokenAsyncTask().execute();
  }

  @Override
  public void onGoogleAPIDisconnected() { onGoogleAPISignedOut(); }

  @Override
  public void onGoogleAPIConnectionError() {}

  @Override
  public void onGoogleAPISignedOut() {
  }


}
