package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.util.HashMap;



public class GoogleAPIHelper extends AsyncTask implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


  private static final String TAG = "CHKLST";

  public static final int STATE_DEFAULT = 0;
  public static final int STATE_SIGN_IN = 1;
  public static final int STATE_IN_PROGRESS = 2;

  private static final int RC_SIGN_IN = 0;

  private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
  private static final String SAVED_PROGRESS = "sign_in_progress";
  private Activity mainContext;

  private GoogleApiClient mGoogleApiClient;
  private NetworkHelper networkClient;

  public int mSignInProgress;

  private PendingIntent mSignInIntent;

  private int mSignInError;


  public GoogleAPIHelper(Activity context, NetworkHelper client) {
    mainContext = context;
    networkClient = client;
    mGoogleApiClient = buildGoogleApiClient();
  }

  private GoogleApiClient buildGoogleApiClient() {
    return new GoogleApiClient.Builder(mainContext)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API, Plus.PlusOptions.builder().build())
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .addScope(Plus.SCOPE_PLUS_PROFILE)
        .build();
  }

  public void setSignInState(int state){
    mSignInProgress = state;
  }

  public void connectClient() {
    mGoogleApiClient.connect();
  }

  public void disconnectClient() {
    if (mGoogleApiClient.isConnected())
      mGoogleApiClient.disconnect();
    networkClient.onGoogleAPISignedOut();
  }

  public HashMap getUserAuthHash(){
    Person me = getCurrentPerson();
    HashMap<String, Object> m = new HashMap<String, Object>();

    m.put("provider", "google_oauth2");
    m.put("uid", me.getId());
    m.put("info", getUserInfoAuthHash(me));

    return m;
  }

  private HashMap getUserInfoAuthHash(Person p){
    HashMap<String, String> m = new HashMap<String, String>();
    m.put("name", p.getName().toString());
    m.put("email", Plus.AccountApi.getAccountName(mGoogleApiClient));
    m.put("first_name", p.getName().getGivenName());
    m.put("last_name", p.getName().getFamilyName());
    m.put("image", p.getImage().getUrl());
    return m;
  }

  private Person getCurrentPerson(){ return Plus.PeopleApi.getCurrentPerson(mGoogleApiClient); }


  public void getAccessToken(){
    AsyncTask<Object, Void, String> task = new AsyncTask<Object, Void, String>() {
      @Override
      protected String doInBackground(Object... params) {
        final String scopes = "oauth2:email profile";

        Bundle appActivities = new Bundle();
        appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, "http://schemas.google.com/AddActivity");

        String token = null;
        try {
          token = GoogleAuthUtil.getToken(mainContext,
              Plus.AccountApi.getAccountName(mGoogleApiClient),
              scopes);
        }
        catch (IOException transientEx) {
          Log.e(TAG, transientEx.toString());
        }
        catch (UserRecoverableAuthException e) {
          // Recover (with e.getIntent())
          Log.e(TAG, e.toString());
          Intent recover = e.getIntent();
          //startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
        }
        catch (GoogleAuthException authEx) {
          Log.e(TAG, authEx.toString());
        }

        return token;
      }

      @Override
      protected void onPostExecute(final String token) {
        Log.i(TAG, "Access token retrieved:" + token);
        HashMap<String, String> credentials = new HashMap<String, String>(){{
          put("token", token);
        }};

        networkClient.onGoogleApiClientTokenReceived(credentials);
      }

    };
    task.execute();
  }

  @Override
  public void onConnected(Bundle connectionHint) {
    Log.i(TAG, "Google API Connected");
    mSignInProgress = STATE_DEFAULT;

    //trigger event for the Main Activity so it can switch to another fragment

    networkClient.onGoogleAPIConnected();
  }

  @Override
  public void onConnectionFailed(ConnectionResult result) {
    Log.i(TAG, "onConnectionFailed, ErrorCode: " + result.getErrorCode());

    if (mSignInProgress != STATE_IN_PROGRESS) {
      mSignInIntent = result.getResolution();
      mSignInError = result.getErrorCode();

      if (mSignInProgress == STATE_SIGN_IN) { //if the user already clicked the sign-in button
        resolveSignInError();
      }
    }

    // In this sample we consider the user signed out whenever they do not have
    // a connection to Google Play services.
    onSignedOut();
  }

  private void resolveSignInError() {
    if (mSignInIntent != null) {
      try {
        mSignInProgress = STATE_IN_PROGRESS;
        mainContext.startIntentSenderForResult(mSignInIntent.getIntentSender(),
            RC_SIGN_IN, null, 0, 0, 0);
      }
      catch (IntentSender.SendIntentException e) {
        Log.i(TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());

        // The intent was canceled before it was sent.  Attempt to connect to
        // get an updated ConnectionResult.
        mSignInProgress = STATE_SIGN_IN;
        mGoogleApiClient.connect();
      }
    }
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case RC_SIGN_IN:
        if (resultCode == Activity.RESULT_OK) {
          // If the error resolution was successful we should continue
          // processing errors.
          mSignInProgress = STATE_SIGN_IN;
        } else {
          // If the error resolution was not successful or the user canceled,
          // we should stop processing errors.
          mSignInProgress = STATE_DEFAULT;
        }

        if (!mGoogleApiClient.isConnecting()) {
          // If Google Play services resolved the issue with a dialog then
          // onStart is not called so we need to re-attempt connection here.
          mGoogleApiClient.connect();
        }
        break;
    }
  }

  private void onSignedOut() {
    // Update the UI to reflect that the user is signed out.
    //mSignInButton.setEnabled(true);
    //trigger signed out event
    networkClient.onGoogleAPISignedOut();
  }

  @Override
  public void onConnectionSuspended(int cause) {
    // The connection to Google Play services was lost for some reason.
    // We call connect() to attempt to re-establish the connection or get a
    // ConnectionResult that we can attempt to resolve.
    mGoogleApiClient.connect();
  }

  @Override
  protected Object doInBackground(Object[] objects) {
    return null;
  }
}
