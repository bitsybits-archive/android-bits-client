package org.bitsybits.bitsclient;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QueryViewHolder extends ChildViewHolder {

  public TextView typeTextView;
  public TextView linkTextView;
  public TextView msgidTextView;
  public TextView inTextView;

  public QueryViewHolder(View itemView) {
    super(itemView);
    typeTextView = (TextView) itemView.findViewById(R.id.query_type);
    linkTextView = (TextView) itemView.findViewById(R.id.query_link);
    msgidTextView = (TextView) itemView.findViewById(R.id.query_msgid);
    inTextView = (TextView) itemView.findViewById(R.id.query_in);
  }
}
