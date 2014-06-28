package edu.upc.lsi.ptdma.checklists.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.CardHeader;

public class DashboardCardHeader extends CardHeader {
  private Integer dataValue;
  private String titleValue;

  public DashboardCardHeader(Context context, String title, Integer data) {
    super(context, R.layout.dashboard_card_header_layout);
    dataValue = data;
    titleValue = title;
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {

    if (view != null) {
      TextView t1 = (TextView) view.findViewById(R.id.dashboard_card_header);
      if (t1 != null)
        t1.setText(titleValue);

      TextView t2 = (TextView) view.findViewById(R.id.dashboard_card_subheader);
      if (t2 != null)
        t2.setText(dataValue.toString() + " " + titleValue);
    }
  }


}
