package com.pereginiak.tcp_server;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketServerTest {
    private Socket socket;
    private static final int SERVER_PORT = 1111;
    private static final String SERVER_IP = "localhost";

    private class TestObj {
        private int number;

        public TestObj(int number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestObj testObj = (TestObj) o;

            return number == testObj.number;
        }

        @Override
        public int hashCode() {
            return number % 2;
        }
    }

    @Test
    public void testClient() throws Exception {
        new Thread(new ClientThread()).start();
        writeToSocket("TestLine");
    }

    private void writeToSocket(String str) throws IOException, InterruptedException {
        int i = 1;
        while (socket == null) {
            System.out.println("waiting for connection..." + i++);
            Thread.sleep(1000);
        }
        System.out.println("write to socket");
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println(str);
        //TODO(romanpe @2018-03-14): need flush?
        out.flush();
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
