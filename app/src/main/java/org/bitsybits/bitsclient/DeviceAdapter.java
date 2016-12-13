package org.bitsybits.bitsclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class DeviceAdapter extends ExpandableRecyclerViewAdapter<DeviceViewHolder, QueryViewHolder> {

  public DeviceAdapter(List<? extends ExpandableGroup> groups) {
    super(groups);
  }

  @Override
  public DeviceViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
    return new DeviceViewHolder(view);
  }

  @Override
  public QueryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_item, parent, false);
    return new QueryViewHolder(view);
  }

  @Override
  public void onBindChildViewHolder(QueryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
    final QueryModel query = ((DeviceModel) group).getItems().get(childIndex);
    holder.typeTextView.setText(query.type);
    holder.linkTextView.setText(query.link);
    holder.msgidTextView.setText(String.valueOf(query.msgid));
    holder.inTextView.setText(String.valueOf(query.in));
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (query.request != null) {
          query.request.run();
        }
      }
    });
  }

  @Override
  public void onBindGroupViewHolder(DeviceViewHolder holder, int flatPosition, ExpandableGroup group) {
    final DeviceModel model = (DeviceModel) group;
    holder.mDeviceAddressTextView.setText(model.address);
    holder.mDeviceMsgTextView.setText(model.message);
    holder.mDeviceCntTextView.setText(String.valueOf(model.counter));
    holder.setRenewClickListener(model.request);
  }
}
