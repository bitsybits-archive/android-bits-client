package org.bitsybits.bitsclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ws4d.coap.core.CoapClient;
import org.ws4d.coap.core.connection.BasicCoapChannelManager;
import org.ws4d.coap.core.connection.api.CoapClientChannel;
import org.ws4d.coap.core.enumerations.CoapRequestCode;
import org.ws4d.coap.core.messages.api.CoapRequest;
import org.ws4d.coap.core.messages.api.CoapResponse;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
  private static final int PORT = 5683;
  private CoapClientChannel mClientChannel = null;
  private TextView content;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    content = (TextView) findViewById(R.id.content);

    mClientChannel = BasicCoapChannelManager.getInstance().connect(new CoapClient() {
      @Override
      public void onResponse(CoapClientChannel channel, CoapResponse response) {

      }

      @Override
      public void onMCResponse(CoapClientChannel channel, final CoapResponse response, final InetAddress srcAddress, int srcPort) {
        content.post(new Runnable() {
          @Override
          public void run() {
            content.setText(content.getText() + "\n" + srcAddress + "\n" + new String(response.getPayload()));
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
      content.setText("");
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
