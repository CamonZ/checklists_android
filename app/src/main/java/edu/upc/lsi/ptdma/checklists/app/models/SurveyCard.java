package edu.upc.lsi.ptdma.checklists.app.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class SurveyCard extends Card {
  private int id;
  private String name;
  private String description;
  private Context appContext;

  public SurveyCard(Context context, JSONObject data){
    super(context);
    appContext = context;
    try {
      id = (Integer) data.get("id");
      name = (String) data.get("name");
      description = (String) data.get("description");

      initializeCardHeader(name);
      setTitle(description);
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public int getSurveyId(){
    return id;
  }

  private void initializeCardHeader(String title){
    CardHeader header = new CardHeader(appContext);
    header.setTitle(title);
    addCardHeader(header);
  }
}
