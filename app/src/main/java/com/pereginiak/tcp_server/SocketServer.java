package com.pereginiak.tcp_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class SocketServer extends Service {

    private ServerSocket serverSocket;

    private static final int SERVER_PORT = 1111;

    private static final String TAG = "SocketServer";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStartCommand" );

        Executors.newSingleThreadExecutor().submit(new ServerThread());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v(TAG,"onDestroy" );
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG,"onBind" );
        //TODO(kasian @2018-03-14):need?
        return null;
    }

    private class ServerThread implements Runnable {
        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Log.i(TAG, "serverSocket.accept()");
                    socket = serverSocket.accept();

                    Log.i(TAG, "new CommunicationThread(socket)");
                    Executors.newSingleThreadExecutor().submit(new CommunicationThread(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class CommunicationThread implements Runnable {
        private Socket clientSocket;
        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String inputLine = input.readLine();

                    if (inputLine == null ){
                        Thread.currentThread().interrupt();
                    }else{
                        String msg = "received from client: " + inputLine;
                        Log.i(TAG, msg);
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        out.write("From server: " + msg);
                        out.newLine();
                        out.flush();

                        sendBroadcastMessage(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void sendBroadcastMessage(String inputLine) {
        Intent broadcastIntent = SocketServerMainActivity.getBroadcastIntent(inputLine);
        sendBroadcast(broadcastIntent);
    }
}
