package org.bitsybits.bitsclient;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ws4d.coap.core.CoapClient;
import org.ws4d.coap.core.connection.BasicCoapChannelManager;
import org.ws4d.coap.core.connection.api.CoapClientChannel;
import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.enumerations.CoapRequestCode;
import org.ws4d.coap.core.messages.api.CoapRequest;
import org.ws4d.coap.core.messages.api.CoapResponse;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private static final int PORT = 5683;
  private CoapClientChannel mClientChannel = null;
  private TextView content;

  private RecyclerView mRecyclerView;
  private DeviceAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  Handler mHandler = new Handler();

  List<DeviceModel> myDataset = new ArrayList<>();
  Map<String, CoapClientChannel> mChannels = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mRecyclerView = (RecyclerView) findViewById(R.id.devices_recycler_view);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new DeviceAdapter(myDataset);
    mRecyclerView.setAdapter(mAdapter);

    content = (TextView) findViewById(R.id.content);

    mClientChannel = BasicCoapChannelManager.getInstance().connect(new CoapClient() {
      @Override
      public void onResponse(CoapClientChannel channel, CoapResponse response) {

      }

      @Override
      public void onMCResponse(CoapClientChannel channel, final CoapResponse response, final InetAddress srcAddress, int srcPort) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            final QueryModel post1 = new QueryModel("POST", "/relay   :1");
            final QueryModel post0 = new QueryModel("POST", "/relay   :0");
            final QueryModel get = new QueryModel("GET", "/relay");
            final DeviceModel device = new DeviceModel(srcAddress.toString(),
                Arrays.asList(post1, post0, get),
                new String(response.getPayload()), 0);

            if (myDataset.contains(device)) {
              myDataset.get(myDataset.indexOf(device)).inc();
            } else {
              myDataset.add(device);
            }
            mAdapter.notifyDataSetChanged();

            if (!mChannels.containsKey(srcAddress.toString())) {
              mChannels.put(srcAddress.toString(), BasicCoapChannelManager.getInstance().connect(new CoapClient() {
                @Override
                public void onResponse(CoapClientChannel channel, CoapResponse response) {
                  myDataset.get(myDataset.indexOf(device)).inc();
                  if (response.getMessageID() == get.savedMsgId) {
                    get.msgid = response.getMessageID();
                    get.in = response.getPayload()[0] - 48;
                  }
                  if (response.getMessageID() == post1.savedMsgId) {
                    post1.msgid = response.getMessageID();
                    post1.in = response.getPayload()[0] - 48;
                  }
                  if (response.getMessageID() == post0.savedMsgId) {
                    post0.msgid = response.getMessageID();
                    post0.in = response.getPayload()[0] - 48;
                  }
                  mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      mAdapter.notifyDataSetChanged();
                    }
                  }, 300);
                  Log.e("onResponse", device.address);

                }

                @Override
                public void onMCResponse(CoapClientChannel channel, CoapResponse response, InetAddress srcAddress, int srcPort) {

                }

                @Override
                public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {

                }
              }, srcAddress, PORT));

              post1.request = new Runnable() {
                @Override
                public void run() {
                  CoapRequest coapRequest = mClientChannel.createRequest(true, CoapRequestCode.POST);
                  coapRequest.setPayload("1");
                  coapRequest.setContentType(CoapMediaType.text_plain);
                  coapRequest.setMulticast(false);
                  coapRequest.setUriPath("/relay");
                  post1.savedMsgId = coapRequest.getMessageID();
                  Log.e("sendMessage", srcAddress.toString());
                  mChannels.get(srcAddress.toString()).sendMessage(coapRequest);
                }
              };

              post0.request = new Runnable() {
                @Override
                public void run() {
                  CoapRequest coapRequest = mClientChannel.createRequest(true, CoapRequestCode.POST);
                  coapRequest.setContentType(CoapMediaType.text_plain);
                  coapRequest.setPayload("0");
                  coapRequest.setMulticast(false);
                  coapRequest.setUriPath("/relay");
                  post0.savedMsgId = coapRequest.getMessageID();
                  Log.e("sendMessage", srcAddress.toString());
                  mChannels.get(srcAddress.toString()).sendMessage(coapRequest);
                }
              };

              get.request = new Runnable() {
                @Override
                public void run() {
                  CoapRequest coapRequest = mClientChannel.createRequest(true, CoapRequestCode.GET);
                  coapRequest.setMulticast(false);
                  coapRequest.setContentType(CoapMediaType.text_plain);
                  coapRequest.setUriPath("/relay");
                  get.savedMsgId = coapRequest.getMessageID();
                  Log.e("sendMessage", srcAddress.toString());
                  mChannels.get(srcAddress.toString()).sendMessage(coapRequest);
                }
              };

              device.request = new Runnable() {
                @Override
                public void run() {
                  CoapRequest coapRequest = mClientChannel.createRequest(true, CoapRequestCode.GET);
                  coapRequest.setMulticast(false);
                  coapRequest.setUriPath("/.well-known/core");
                  Log.e("sendMessage", srcAddress.toString());
                  mChannels.get(srcAddress.toString()).sendMessage(coapRequest);
                }
              };
            }
          }
        });
      }

      @Override
      public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {

      }
    }, InetUtils.getBroadcastAddress(InetUtils.getNetworkInterface()), PORT);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        CoapRequest coapRequest = mClientChannel.createRequest(true, CoapRequestCode.GET);
        coapRequest.setMulticast(true);
        coapRequest.setUriPath("/.well-known/core");
        mClientChannel.sendMessage(coapRequest);
        Snackbar.make(view, "/.well-known/core", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_clear) {
      myDataset.clear();
      mChannels.clear();
      mAdapter.notifyDataSetChanged();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
