package com.pereginiak.tcp_server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

/*
        Intent socketServiceIntent = new Intent(this, SocketServer.class);
        startService(socketServiceIntent);

        registerBroadcastReceiver();
        registerLocalBroadcastReceiver();
*/

        //showLogcat();
        //startWebServer();
    }

    public void startAnotherApp (View view) {
        Intent intent = new Intent();
        //intent.setClassName("com.pereginiak.gateway1c.nfc", "com.pereginiak.gateway1c.nfc.NfcReader");
        intent.setClassName("com.pereginiak.cardscanner", "com.pereginiak.cardscanner.CardScannerActivity");
        startActivityForResult(intent, 1);

/*
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;
        Intent intent = new Intent(Intent.ACTION_MAIN, );
*/

//TODO(kasian @2018-06-24): working example
/*
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("com.pereginiak.gateway1c");
        if (intent == null) {
            Log.intent(TAG, "Application is not installed:" + intent);
        } else {
            startActivity(intent);
        }
*/

/*
        Does not work by some means:(
        Intent intent = new Intent(Intent.ACTION_RUN);
        intent.setComponent(new ComponentName("com.pereginiak.gateway1c","com.pereginiak.gateway1c.MainActivity"));
        startActivity(intent);
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                showAlert(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                showAlert("Cancelled");
            }
        }
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

    private void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Response");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showLog(String inputMessage) {
        TextView outputView = (TextView) findViewById(R.id.outputView);
        outputView.append("\n");
        outputView.append(inputMessage);
    }

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
}
