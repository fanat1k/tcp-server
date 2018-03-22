package com.pereginiak.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBroadcastReceiver extends BroadcastReceiver {
    public static final String MESSAGE = "com.pereginiak.tcp_server.MESSAGE";
    private static final String TAG = "TestBroadcastReceiver";

    private ReceiverMainActivity mainActivity;

    public TestBroadcastReceiver(ReceiverMainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String inputMessage = intent.getStringExtra(MESSAGE);
        Log.i(TAG, "receive broadcast: " + inputMessage);

        sendBroadcastMessage(inputMessage);
        //EditText editText = (EditText) findViewById(R.id.broadcastText);
    }

    private void sendBroadcastMessage(String inputLine) {
        Intent broadcastIntent = new Intent(ReceiverMainActivity.LOCAL_BROADCAST_TOPIC);
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadcastIntent.putExtra(ReceiverMainActivity.EXTRA_MESSAGE, inputLine);

        mainActivity.sendBroadcast(broadcastIntent);
    }

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }
        @Override
        public void run() {
            //text.setText(text.getText().toString()+"Client Says: "+ msg + "\n");
        }
    }

}
