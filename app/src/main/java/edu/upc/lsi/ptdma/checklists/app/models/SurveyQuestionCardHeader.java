package edu.upc.lsi.ptdma.checklists.app.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.lsi.ptdma.checklists.app.R;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class SurveyQuestionCardHeader extends CardHeader {
  private String titleValue;

  public SurveyQuestionCardHeader(Context context, String title) {
    super(context, R.layout.survey_question_card_header_layout);
    titleValue = title;
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {
    if (view != null) {
      TextView t1 = (TextView) view.findViewById(R.id.survey_question_number);
      if (t1 != null)
        t1.setText(titleValue);
    }
  }
}
