package com.pereginiak.tcp_server;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private BroadcastListener broadcastListener;

    private BroadcastReceiver localBroadcastListener;

    private static final String TAG = "MainActivity";

    private static final String EXTRA_MESSAGE = "com.pereginiak.tcp_server.MESSAGE";

    private static final String BROADCAST_NOTIFICATION = "com.pereginiak.tcp_server.BROADCAST_NOTIFICATION";

    private static final String INCOMING_BROADCAST = "com.pereginiak.tcp_server.INCOMING_BROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent socketServiceIntent = new Intent(this, SocketServer.class);
        startService(socketServiceIntent);

        registerBroadcastReceiver();
        registerLocalBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        this.unregisterReceiver(broadcastListener);
        this.unregisterReceiver(localBroadcastListener);
    }

    public void sendBroadcast (View view) {
        EditText editText = (EditText) findViewById(R.id.broadcastText);
        String message = editText.getText().toString();
        Log.i(TAG, "input broadcast message: " + message);

        Intent broadcastIntent = getBroadcastIntent(message);

        Log.i(TAG, "sendBroadcast: " + broadcastIntent);
        sendBroadcast(broadcastIntent);
    }

    public static Intent getBroadcastIntent(String message) {
        Intent intent = new Intent(BROADCAST_NOTIFICATION);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(EXTRA_MESSAGE, message);

        return intent;
    }

    private void registerBroadcastReceiver() {
        Log.i(TAG, "registerBroadcastReceiver");

        broadcastListener = new BroadcastListener();
        //TODO(kasian @2018-03-20): set INCOMING_BROADCAST
        //IntentFilter filter = new IntentFilter(INCOMING_BROADCAST);
        IntentFilter filter = new IntentFilter(BROADCAST_NOTIFICATION);
        this.registerReceiver(broadcastListener, filter);
    }

    private void registerLocalBroadcastReceiver() {
        Log.i(TAG, "registerLocalBroadcastReceiver");

        localBroadcastListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceiveLocalBroadcast");
                TextView outputView = (TextView) findViewById(R.id.outputView);
                outputView.setText("123");
            }
        };

        IntentFilter filter = new IntentFilter(Constants.LOCAL_BROADCAST_NOTIFICATION);
        this.registerReceiver(localBroadcastListener, filter);
    }
}
