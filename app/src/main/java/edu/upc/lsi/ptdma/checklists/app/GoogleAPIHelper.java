package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;


public class GoogleAPIHelper implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


  private static final String TAG = "Checklists";

  public static final int STATE_DEFAULT = 0;
  public static final int STATE_SIGN_IN = 1;
  public static final int STATE_IN_PROGRESS = 2;

  private static final int RC_SIGN_IN = 0;

  private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
  private static final String SAVED_PROGRESS = "sign_in_progress";
  private Activity mainContext;

  private GoogleApiClient mGoogleApiClient;

  public int mSignInProgress;

  private PendingIntent mSignInIntent;

  private int mSignInError;


  public GoogleAPIHelper(Activity context) {
    mainContext = context;
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
  }

  public String getAccessToken(){
    Bundle appActivities = new Bundle();
    appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, "<REVIEW_ACTIVITY>");
    String scopes = "oauth2:server:client_id:<SERVER-CLIENT-ID>:api_scope:email profile";
    String accessToken = null;

    try {

      accessToken = GoogleAuthUtil.getToken(mainContext,
        Plus.AccountApi.getAccountName(mGoogleApiClient),  // String accountName
        scopes,                                            // String scope
        appActivities                                      // Bundle bundle
      );
    }


    catch (IOException transientEx) {
      // network or server error, the call is expected to succeed if you try again later.
      // Don't attempt to call again immediately - the request is likely to
      // fail, you'll hit quotas or back-off.

      return null;
    }
    catch (UserRecoverableAuthException e) {
      // Recover
      accessToken = null;
    }
    catch (GoogleAuthException authEx) {
      // Failure. The call is not expected to ever succeed so it should not be
      // retried.
      return null;
    }
    catch (Exception e) { throw new RuntimeException(e); }
    return accessToken;
  }

  @Override
  public void onConnected(Bundle connectionHint) {
    Log.i(TAG, "Google API Connected");
    mSignInProgress = STATE_DEFAULT;

    //trigger event for the Main Activity so it can switch to another fragment

    ((GoogleAPIHelperEvents) mainContext).onGoogleAPIConnected();
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
  }

  @Override
  public void onConnectionSuspended(int cause) {
    // The connection to Google Play services was lost for some reason.
    // We call connect() to attempt to re-establish the connection or get a
    // ConnectionResult that we can attempt to resolve.
    mGoogleApiClient.connect();
  }
}
