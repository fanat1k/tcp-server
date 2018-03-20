package com.pereginiak.tcp_server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastListener extends BroadcastReceiver {
    public static final String MESSAGE = "com.pereginiak.tcp_server.MESSAGE";
    private static final String TAG = "TestBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String inputMessage = intent.getStringExtra(MESSAGE);
        Log.i(TAG, "onReceive: " + inputMessage);

        sendMessageToActivity(context, inputMessage);
    }

    public static void sendMessageToActivity(Context context, String message) {
        Log.i(TAG, "sendMessageToActivity: " + message);

        Intent localBroadcastIntent = new Intent(Constants.LOCAL_BROADCAST_NOTIFICATION);
        localBroadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        localBroadcastIntent.putExtra(Constants.EXTRA_MESSAGE, message);

        context.sendBroadcast(localBroadcastIntent);
    }

    class UpdateUIThread implements Runnable {
        private String msg;

        public UpdateUIThread(String str) {
            this.msg = str;
        }
        @Override
        public void run() {
            //text.setText(text.getText().toString()+"Client Says: "+ msg + "\n");
        }
    }
}
