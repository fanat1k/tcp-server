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

    //TODO(kasian @2018-03-14): moved to onStartCommand
/*
    @Override
    public void onCreate() {
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
    }
*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStartCommand" );

        Executors.newSingleThreadExecutor().submit(new ServerThread());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG,"onBind" );
        //TODO(kasian @2018-03-14):
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
                    String read = input.readLine();

                    if (read == null ){
                        Thread.currentThread().interrupt();
                    }else{
                        String msg = "received from client: " + read;
                        Log.i(TAG, msg);
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        out.write("From server: " + msg);
                        out.newLine();
                        out.flush();
                        //updateConversationHandler.post(new updateUIThread(read));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


/*
    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            text.setText("Client Says: "+ msg + new Date() + "\n");
        }
    }
*/
}
