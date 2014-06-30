package edu.upc.lsi.ptdma.checklists.app.network;

import java.util.HashMap;

public interface GoogleAPIHelperListener {
  public void onGoogleAPIConnected();

  public void onGoogleAPIDisconnected();

  public void onGoogleAPIConnectionError();

  public void onGoogleAPISignedOut();

  public void onGoogleApiClientTokenReceived(HashMap credentials);
}
