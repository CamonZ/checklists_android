package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import java.util.HashMap;

import edu.upc.lsi.ptdma.checklists.app.NavigationDrawerFragment.NavigationDrawerCallbacks;


public class MainActivity extends Activity implements
    OnSignInFragmentListener,
    NetworkEventsListener {

  private NavigationDrawerFragment navigationDrawer;
  private SignInFragment signInScreen;

  private DrawerEventsListener drawerEventsHandler;

  private CharSequence mTitle;

  private NetworkHelper connectionClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTitle = getTitle();
    connectionClient = new NetworkHelper(this);

    drawerEventsHandler = new DrawerEventsListener(this);

    setUpNavigationDrawer();
    setUpLoginFragment();
  }

  private void setUpNavigationDrawer() {
    navigationDrawer = (NavigationDrawerFragment) getFragmentManager().
        findFragmentById(R.id.navigation_drawer);

    navigationDrawer.setUp(
        R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));

    navigationDrawer.setCallbacksHandler(drawerEventsHandler);
    navigationDrawer.disableDrawer();
  }

  private void setUpLoginFragment() {
    if (signInScreen == null)
      signInScreen = SignInFragment.newInstance();

    getFragmentManager().
        beginTransaction().
        replace(R.id.container, signInScreen).
        commit();
  }

  public void onSectionAttached(int number) {
    switch (number) {
      case 0:
        mTitle = getString(R.string.title_section1);
        break;
      case 1:
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

  public void logOut() {
    connectionClient.signOut();
  }

  public void signedInToAPIs() {
    HashMap<String, Integer> h = new HashMap<String, Integer>(){{
      put("surveys", 100);
      put("incidences", 127);
    }};

    getFragmentManager().
        beginTransaction().
        replace(R.id.container, DashboardFragment.newInstance(connectionClient, h)).
        commit();

    navigationDrawer.enableDrawer();
  }

  public void signedOutToAPIs() {
    setUpLoginFragment();
  }

  public NavigationDrawerCallbacks getDrawerHandler() {
    return drawerEventsHandler;
  }

  public void onSignInButtonClicked() {
    connectionClient.signIn();
  }
}
