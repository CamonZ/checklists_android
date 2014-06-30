package edu.upc.lsi.ptdma.checklists.app.models;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.lsi.ptdma.checklists.app.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.component.CardThumbnailView;

public class SurveyQuestionCard extends Card implements View.OnClickListener{
  private int questionId;
  private Integer questionNumber;
  private String questionText;
  private boolean questionValue = false;
  private ToggleButton answerButton;



  public SurveyQuestionCard(Activity activity, JSONObject data, int questionNum) throws JSONException{
    super(activity, R.layout.survey_question_card_layout);

    questionNumber = questionNum;
    questionId = (Integer)data.get("id");
    questionText = (String)data.get("question");


    SurveyQuestionCardHeader header = new SurveyQuestionCardHeader(activity,
        "Question " + this.questionNumber.toString());

    addCardHeader(header);
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {
    TextView questionText = (TextView) view.findViewById(R.id.survey_question_text);
    questionText.setText(this.questionText);
    setAnswerButton(view);
  }

  public int getQuestionId(){ return questionId; }
  public boolean getQuestionValue(){ return questionValue; }

  private void setAnswerButton(View view){
    answerButton = (ToggleButton) view.findViewById(R.id.question_answer_button);

    answerButton.setTextOn("Yes");
    answerButton.setTextOff("No");
    answerButton.setChecked(false);
    answerButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    questionValue = ((ToggleButton)view).isChecked();
  }

  public JSONObject toJSONObject() throws JSONException {
    JSONObject json = new JSONObject();
    json.put("check_item_id", questionId);
    json.put("response", questionValue);
    return json;
  }
}
