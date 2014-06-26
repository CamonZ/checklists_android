package edu.upc.lsi.ptdma.checklists.app;

public interface GoogleAPIHelperListener {
  public void onGoogleAPIConnected();

  public void onGoogleAPIDisconnected();

  public void onGoogleAPIConnectionError();

  public void onGoogleAPISignedOut();
}
