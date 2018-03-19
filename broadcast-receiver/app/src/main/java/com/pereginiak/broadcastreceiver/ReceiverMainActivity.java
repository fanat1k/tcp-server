package com.pereginiak.broadcastreceiver;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class ReceiverMainActivity extends Activity {
    private static final String BROADCAST_NOTIFICATION = "com.pereginiak.tcp_server.BROADCAST_NOTIFICATION";
    private static final String TAG = "ReceiverMainActivity";

    TestBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        registerBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        this.unregisterReceiver(broadcastReceiver);
    }

    private void registerBroadcastReceiver() {
        Log.i(TAG, "registerBroadcastReceiver");

        broadcastReceiver = new TestBroadcastReceiver();
        IntentFilter filter = new IntentFilter(BROADCAST_NOTIFICATION);
        this.registerReceiver(broadcastReceiver, filter);
    }
}
