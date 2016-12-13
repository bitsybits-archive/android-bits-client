package org.bitsybits.bitsclient;

import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class DeviceViewHolder extends GroupViewHolder {
  private ImageView mArrowImageView;
  private ImageView mAutoRenewImageView;
  private View mAutoRenewPlaceView;
  public TextView mDeviceAddressTextView;
  public TextView mDeviceMsgTextView;
  public TextView mDeviceCntTextView;

  public DeviceViewHolder(View itemView) {
    super(itemView);
    mDeviceAddressTextView = (TextView) itemView.findViewById(R.id.device_address);
    mDeviceMsgTextView = (TextView) itemView.findViewById(R.id.device_msg);
    mDeviceCntTextView = (TextView) itemView.findViewById(R.id.device_counter);
    mArrowImageView = (ImageView) itemView.findViewById(R.id.device_arrow);
    mAutoRenewPlaceView = itemView.findViewById(R.id.device_autorenew_place);
    mAutoRenewImageView = (ImageView) itemView.findViewById(R.id.device_autorenew_icon);
  }

  @Override
  public void expand() {
    animateExpand();
  }

  @Override
  public void collapse() {
    animateCollapse();
  }

  public void setRenewClickListener(final Runnable listener) {
    mAutoRenewPlaceView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (listener != null) {
          listener.run();
        }
        Log.e("asdasdasdasd", "ASDASDASD");
        RotateAnimation rotate =
            new RotateAnimation(0, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        mAutoRenewImageView.startAnimation(rotate);
      }
    });
  }

  private void animateExpand() {
    RotateAnimation rotate =
        new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    mArrowImageView.setAnimation(rotate);
  }

  private void animateCollapse() {
    RotateAnimation rotate =
        new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    mArrowImageView.setAnimation(rotate);
  }
}
