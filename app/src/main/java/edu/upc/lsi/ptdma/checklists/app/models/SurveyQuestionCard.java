package edu.upc.lsi.ptdma.checklists.app.models;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.lsi.ptdma.checklists.app.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.component.CardThumbnailView;

public class SurveyQuestionCard extends Card {
  private int questionId;
  private Integer questionNumber;
  private String questionText;
  private boolean questionValue;



  public SurveyQuestionCard(Activity activity, JSONObject data, int questionNum) throws JSONException{
    super(activity, R.layout.survey_question_card_layout);

    questionNumber = questionNum;
    questionId = (Integer)data.get("id");
    questionText = (String)data.get("question");
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {

    TextView questionNumber = (TextView) view.findViewById(R.id.survey_question_number);
    questionNumber.setText("Question " + this.questionNumber.toString());


    TextView questionText = (TextView) view.findViewById(R.id.survey_question_text);
    questionText.setText(this.questionText);

    //finish setting up toggleButton here.
  }

  public int getQuestionId(){ return questionId; }
  public boolean getQuestionValue(){ return questionValue; }


}
