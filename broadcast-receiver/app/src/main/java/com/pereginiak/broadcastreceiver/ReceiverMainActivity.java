package com.pereginiak.broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ReceiverMainActivity extends Activity {
    private static final String BROADCAST_NOTIFICATION = "com.e1c.mobile.PushNotificationReceiver";
    public static final String LOCAL_BROADCAST_TOPIC = "com.pereginiak.broadcastreceiver.NOTIFY_MAIN_LOCAL";
    private static final String TAG = "ReceiverMainActivity";
    public static final String EXTRA_MESSAGE = "com.pereginiak.broadcastreceiver.MESSAGE_LOCAL";

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

        broadcastReceiver = new TestBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter(BROADCAST_NOTIFICATION);
        this.registerReceiver(broadcastReceiver, filter);

        registerLocalBroadcastReceiver();
    }

    private void registerLocalBroadcastReceiver() {
        Log.i(TAG, "registerLocalBroadcastReceiver");

        BroadcastReceiver localBroadcastListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String inputMessage = intent.getStringExtra(EXTRA_MESSAGE);
                Log.i(TAG, "onReceiveLocalBroadcast: " + inputMessage);

                showLog("receive broadcast:" + inputMessage);
            }
        };

        IntentFilter filter = new IntentFilter(LOCAL_BROADCAST_TOPIC);
        this.registerReceiver(localBroadcastListener, filter);
    }

    private void showLog(String inputMessage) {
        TextView outputView = (TextView) findViewById(R.id.outputView);
        outputView.append("\n");
        outputView.append(inputMessage);
    }
}
