package edu.upc.lsi.ptdma.checklists.app;

public interface GoogleAPIHelperEvents {
    public void onGoogleAPIConnected();
    public void onGoogleAPIDisconnected();
    public void onGoogleAPIConnectionError();
    public void onGoogleAPISignedOut();
}
