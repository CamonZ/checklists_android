package edu.upc.lsi.ptdma.checklists.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SigninFragment extends Fragment implements View.OnClickListener{
  private OnSigninListener activityListener;

  public SigninFragment() {}

  public static SigninFragment newInstance() {
    SigninFragment fragment = new SigninFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_signin, container, false);
    v.findViewById(R.id.sign_in_button).setOnClickListener(this);
    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activityListener = (OnSigninListener) activity;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityListener = null;
  }

  @Override
  public void onClick(View view) { activityListener.onSignInButtonClicked(); }

}
