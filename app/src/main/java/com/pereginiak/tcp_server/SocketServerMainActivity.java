package com.pereginiak.tcp_server;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SocketServerMainActivity extends Activity {

    public static final String EXTRA_MESSAGE = "com.pereginiak.tcp_server.MESSAGE";

    private static final String TAG = "MainActivity";
    private static final String BROADCAST_NOTIFICATION = "com.pereginiak.tcp_server.BROADCAST_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        Intent socketServiceIntent = new Intent(this, SocketServer.class);
        getApplicationContext().startService(socketServiceIntent);
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
}
