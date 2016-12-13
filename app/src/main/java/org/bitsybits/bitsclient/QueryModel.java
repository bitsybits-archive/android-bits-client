package org.bitsybits.bitsclient;

import android.os.Parcel;
import android.os.Parcelable;

public class QueryModel implements Parcelable {
  public String type;
  public String link;
  public int msgid = 0;
  public int in = 0;
  public Runnable request;

  public int savedMsgId = 0;

  public QueryModel(final String tp, final String lnk) {
    type = tp;
    link = lnk;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.type);
    dest.writeString(this.link);
    dest.writeInt(this.msgid);
    dest.writeInt(this.in);
  }

  protected QueryModel(Parcel in) {
    this.type = in.readString();
    this.link = in.readString();
    this.msgid = in.readInt();
    this.in = in.readInt();
  }

  public static final Creator<QueryModel> CREATOR = new Creator<QueryModel>() {
    public QueryModel createFromParcel(Parcel source) {
      return new QueryModel(source);
    }

    public QueryModel[] newArray(int size) {
      return new QueryModel[size];
    }
  };
}
