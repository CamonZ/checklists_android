package edu.upc.lsi.ptdma.checklists.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.SignInButton;

import edu.upc.lsi.ptdma.checklists.app.R;


public class SignInFragment extends Fragment implements
    View.OnClickListener {
  private OnSignInFragmentListener activityListener;

  public SignInFragment() {
  }

  public static SignInFragment newInstance() {
    return new SignInFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

    SignInButton button = (SignInButton) v.findViewById(R.id.sign_in_button);

    if (button != null)
      button.setOnClickListener(this);

    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activityListener = (OnSignInFragmentListener) activity;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityListener = null;
  }

  @Override
  public void onClick(View view) {
    activityListener.onSignInButtonClicked();
  }
}
