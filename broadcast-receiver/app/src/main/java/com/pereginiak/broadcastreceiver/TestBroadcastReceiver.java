package com.pereginiak.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBroadcastReceiver extends BroadcastReceiver {
    public static final String MESSAGE = "com.pereginiak.tcp_server.MESSAGE";
    private static final String TAG = "TestBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String inputMessage = intent.getStringExtra(MESSAGE);
        Log.i(TAG, "receive broadcast: " + inputMessage);

        //EditText editText = (EditText) findViewById(R.id.broadcastText);
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
