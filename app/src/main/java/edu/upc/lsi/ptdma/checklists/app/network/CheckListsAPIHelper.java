package edu.upc.lsi.ptdma.checklists.app.network;

import android.app.Activity;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CheckListsAPIHelper {
  private static final String HOST = "http://192.168.1.101:3000/";
  private static final String BASE_API_URL = HOST + "api/v1/";
  private static final String CLIENT_ID = "f0eef85aac2e6a151c4784af0bcf220f6735bbf26f20143119695c27321dc2cb";
  private static final String CLIENT_SECRET = "ffe41db185119b72ca02fa3292c867a460f3fdf07431b3a80fe6702155553b6e";
  private static AsyncHttpClient httpClient;
  private JSONObject userDataHash;
  private NetworkHelper networkManager;
  private String UserUID;
  private Activity mainContext;

  public CheckListsAPIHelper(NetworkHelper helper) {
    networkManager = helper;
    mainContext = (Activity) networkManager.applicationContext();
    httpClient = new AsyncHttpClient();
    httpClient.addHeader("Host", HOST);
  }

  public void startSignInFlow() {
    getClientGrantToken();
  }


  private void getClientGrantToken() {
    httpClient.post(HOST + "oauth/token", clientCredentialsTokenRequestParams(), new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        //{"access_token":"token","token_type":"bearer","expires_in":xxx}

        try {
          String clientCredentialsToken = (String) response.get("access_token");
          httpClient.addHeader("Authorization", "Bearer " + clientCredentialsToken);
          signInUser();
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }

      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
      }

    });
  }

  private void signInUser() {
    try {
      ByteArrayEntity entity = new ByteArrayEntity(userDataHash.toString().getBytes("UTF-8"));
      httpClient.post(
          mainContext,
          BASE_API_URL + "users/sign_in",
          entity,
          "application/json",
          new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              try {
                // add the uid of the user to the header
                UserUID = (String) userDataHash.get("uid");
                httpClient.addHeader("UserUID", UserUID);
                networkManager.onChecklistsAPIConnected();
              } catch (JSONException e) {
                throw new RuntimeException(e);
              }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
            }

          }
      );
    } catch (UnsupportedEncodingException e) {

    }
  }

  private RequestParams clientCredentialsTokenRequestParams() {
    RequestParams params = new RequestParams();
    params.add("grant_type", "client_credentials");
    params.add("client_id", CLIENT_ID);
    params.add("client_secret", CLIENT_SECRET);
    return params;
  }

  public void setUserSignInRequestParams(JSONObject params) {
    userDataHash = params;
  }

  public void getDashboardData() {
    httpClient.get(BASE_API_URL + "dashboard", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        networkManager.onDashboardDataReceived(response);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
      }
    });
  }

  public void getChecklists() {
    httpClient.get(BASE_API_URL + "checklists", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {}

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        networkManager.onChecklistsReceived(response);
      }
    });

  }

  public void getChecklist(int id) {
    httpClient.get(BASE_API_URL + "checklists/" + id, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        networkManager.onChecklistReceived(response);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
      }
    });

  }

  public void postSurvey(JSONObject data) {
    try {
      ByteArrayEntity entity = new ByteArrayEntity(data.toString().getBytes("UTF-8"));
      httpClient.post(
          mainContext,
          BASE_API_URL + "surveys",
          entity,
          "application/json",
          new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              networkManager.onSurveySent();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
            }

          }
      );
    } catch (UnsupportedEncodingException e) {

    }

  }
}
