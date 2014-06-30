package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.HashMap;

import edu.upc.lsi.ptdma.checklists.app.fragments.DashboardFragment;
import edu.upc.lsi.ptdma.checklists.app.MainActivity;
import edu.upc.lsi.ptdma.checklists.app.fragments.NavigationDrawerFragment;
import edu.upc.lsi.ptdma.checklists.app.fragments.PlaceHolderFragment;
import edu.upc.lsi.ptdma.checklists.app.R;
import edu.upc.lsi.ptdma.checklists.app.fragments.SurveysFragment;
import edu.upc.lsi.ptdma.checklists.app.network.NetworkHelper;

public class DrawerEventsListener implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  private MainActivity mainActivity;
  private NetworkHelper networkManager;

  private HashMap<Integer, Class> fragmentClassForPosition = new HashMap<Integer, Class>(){{
    put(0, DashboardFragment.class);
    put(1, SurveysFragment.class);
    //put(2, IncidencesFragment.class);

  }};

  public DrawerEventsListener(MainActivity context) {
    mainActivity = context;
    networkManager = mainActivity.getNetworkManager();
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // update the main content by replacing fragments

    if (position < 3) {

      FragmentManager fragmentManager = mainActivity.getFragmentManager();
      fragmentManager.beginTransaction().
          replace(R.id.container, PlaceHolderFragment.newInstance(position + 1)).
          commit();

    }
    else {
      mainActivity.logOut();
    }
  }

  private Fragment fragmentForPosition(int position){
    Fragment fragment;
    try {
      fragment = (Fragment)(fragmentClassForPosition.get(position)).
          getMethod("newInstance").
          invoke(null, networkManager);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    return fragment;
  }
}
