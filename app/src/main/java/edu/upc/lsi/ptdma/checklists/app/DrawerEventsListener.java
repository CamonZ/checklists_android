package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class DrawerEventsListener implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  private Activity mainActivity;

  public DrawerEventsListener(MainActivity context) {
    mainActivity = context;
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // update the main content by replacing fragments
    FragmentManager fragmentManager = mainActivity.getFragmentManager();
    fragmentManager.beginTransaction().
        replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).
        commit();
  }
}
