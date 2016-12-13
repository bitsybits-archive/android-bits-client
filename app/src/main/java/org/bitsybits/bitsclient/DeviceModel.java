package org.bitsybits.bitsclient;

import android.annotation.SuppressLint;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

@SuppressLint("ParcelCreator")
public class DeviceModel extends ExpandableGroup<QueryModel> {
  public String address;
  public String message;
  public int counter;
  public Runnable request;

  public DeviceModel(String addr, List<QueryModel> items, String msg, int cnt) {
    super(addr, items);
    address = addr;
    message = msg;
    counter = cnt;
  }

  public int inc() {
    return ++counter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DeviceModel that = (DeviceModel) o;

    if (address != null ? !address.equals(that.address) : that.address != null) return false;
    return message != null ? message.equals(that.message) : that.message == null;
  }

  @Override
  public int hashCode() {
    int result = address != null ? address.hashCode() : 0;
    result = 31 * result + (message != null ? message.hashCode() : 0);
    return result;
  }
}
