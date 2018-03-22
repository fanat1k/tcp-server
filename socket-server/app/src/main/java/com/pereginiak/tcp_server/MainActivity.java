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

import java.io.IOException;

public class MainActivity extends Activity {

    private BroadcastListener broadcastListener;

    private BroadcastReceiver localBroadcastListener;

    private static final String TAG = "MainActivity";

    private static final String EXTRA_MESSAGE = "com.pereginiak.tcp_server.MESSAGE";

    private static final String OUTGOING_NOTIFICATION = "com.e1c.mobile.PushNotificationReceiver";

    private static final String INCOMING_BROADCAST = "com.e1c.mobile.PushNotificationReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent socketServiceIntent = new Intent(this, SocketServer.class);
        startService(socketServiceIntent);

        registerBroadcastReceiver();
        registerLocalBroadcastReceiver();

        //showLogcat();
        //startWebServer();
    }

    private void startWebServer() {
        WebServerTest webServer = new WebServerTest(8090);
        try {
            webServer.start();
        } catch (IOException e) {
            Log.i(TAG, "ERROR:" + e);
            e.printStackTrace();
        }
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
        showLog("send broadcast:" + broadcastIntent);
    }

    public static Intent getBroadcastIntent(String message) {
        Intent intent = new Intent(OUTGOING_NOTIFICATION);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(EXTRA_MESSAGE, message);

        return intent;
    }

    private void registerBroadcastReceiver() {
        Log.i(TAG, "registerBroadcastReceiver");

        broadcastListener = new BroadcastListener();
        IntentFilter filter = new IntentFilter(INCOMING_BROADCAST);

        this.registerReceiver(broadcastListener, filter);
    }

    private void registerLocalBroadcastReceiver() {
        Log.i(TAG, "registerLocalBroadcastReceiver");

        localBroadcastListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String inputMessage = intent.getStringExtra(Constants.EXTRA_MESSAGE);
                Log.i(TAG, "onReceiveLocalBroadcast: " + inputMessage);

                showLog("receive broadcast:" + inputMessage);
            }
        };

        IntentFilter filter = new IntentFilter(Constants.LOCAL_BROADCAST_NOTIFICATION);
        this.registerReceiver(localBroadcastListener, filter);
    }

    private void showLog(String inputMessage) {
        TextView outputView = (TextView) findViewById(R.id.outputView);
        outputView.append("\n");
        outputView.append(inputMessage);
    }

/*
    private void showLogcat() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            TextView tv = (TextView)findViewById(R.id.outputView);
            tv.setText(log.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
