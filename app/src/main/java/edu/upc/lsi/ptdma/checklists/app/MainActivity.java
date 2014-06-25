package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import edu.upc.lsi.ptdma.checklists.app.NavigationDrawerFragment.NavigationDrawerCallbacks;


public class MainActivity extends Activity implements GoogleAPIHelperEvents, OnSigninListener {

  private NavigationDrawerFragment navigationDrawer;
  private SigninFragment signInScreen;



  private DrawerEventsListener drawerEventsHandler;

  private CharSequence mTitle;

  private GoogleAPIHelper googleConnectionHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTitle = getTitle();

    googleConnectionHelper = new GoogleAPIHelper(this);

    drawerEventsHandler = new DrawerEventsListener(this);
    setUpNavigationDrawer();

    setUpLoginFragment();
  }

  private void setUpNavigationDrawer(){
    navigationDrawer = (NavigationDrawerFragment)getFragmentManager().
        findFragmentById(R.id.navigation_drawer);

    navigationDrawer.setUp(
        R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));

    navigationDrawer.disableDrawer();
  }

  private void setUpLoginFragment(){
    if(signInScreen == null)
      signInScreen = SigninFragment.newInstance();

    getFragmentManager().
        beginTransaction().
        replace(R.id.container, signInScreen).
        commit();
  }

  public void onSectionAttached(int number) {
    switch (number) {
      case 1:
        mTitle = getString(R.string.title_section1);
        break;
      case 2:
        mTitle = getString(R.string.title_section2);
        break;
    }
  }

  public void restoreActionBar() {
    ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setTitle(mTitle);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (!navigationDrawer.isDrawerOpen()) {
      // Only show items in the action bar relevant to this screen
      // if the drawer is not showing. Otherwise, let the drawer
      // decide what to show in the action bar.
      getMenuInflater().inflate(R.menu.main, menu);
      restoreActionBar();
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onGoogleAPIConnected() {
    getFragmentManager().
        beginTransaction().
        replace(R.id.container, PlaceholderFragment.newInstance(0)).
        commit();

    navigationDrawer.enableDrawer();
  }

  @Override
  public void onGoogleAPIDisconnected() { onGoogleAPISignedOut(); }

  @Override
  public void onGoogleAPIConnectionError() {}

  @Override
  public void onGoogleAPISignedOut() {
    navigationDrawer.disableDrawer();
    setUpLoginFragment();
  }

  public NavigationDrawerCallbacks getDrawerHandler() { return drawerEventsHandler; }

  @Override
  public void onSignInButtonClicked() {
    googleConnectionHelper.setSignInState(GoogleAPIHelper.STATE_SIGN_IN);
    googleConnectionHelper.connectClient();
  }
}
