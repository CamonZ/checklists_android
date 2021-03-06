package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.upc.lsi.ptdma.checklists.app.MainActivity;
import edu.upc.lsi.ptdma.checklists.app.R;

public class PlaceHolderFragment extends Fragment {
  /**
   * Returns a new instance of this fragment for the given section
   * number.
   */
  public static PlaceHolderFragment newInstance(int sectionNumber) {
    PlaceHolderFragment fragment = new PlaceHolderFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * The fragment argument representing the section number for this
   * fragment.
   */
  private static final String ARG_SECTION_NUMBER = "section_number";

  public PlaceHolderFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    return rootView;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(
        getArguments().getInt(ARG_SECTION_NUMBER));
  }
}