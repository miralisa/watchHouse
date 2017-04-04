package com.anastasiia.watchhouse;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by anastasiia on 01.04.17.
 */
public class Server {
    ServerActivity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 5555;
    String tag = "@--------@";

    public Server(ServerActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                Log.d("@--onDestroy---@", "socket is closed ? "+serverSocket.isClosed());
                serverSocket.close();
                Log.d("@--onDestroy---@", "socket is closed ? "+serverSocket.isClosed());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(socketServerPORT);
                Log.d(message,"Creation socket");

                while(true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    count++;

                    Log.d("@-------@", "socket acceptee:"+socket.toString());
                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
                cnt = c;
        }

        public String envoi(String msg) {
            Log.d(tag, "debut envoi");
            DataOutputStream outputStream = null;
            try {
                outputStream = new DataOutputStream(hostThreadSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                message = "IOException: " + e.toString();

            }
            try {
                outputStream.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
                message = "IOException: " + e.toString();

            }
            return message;
        }

        public String reception() {
            Log.d(tag, "debut reception");
            DataInputStream inputStream = null;
            try {
                inputStream = new DataInputStream(hostThreadSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                message = "IOException: " + e.toString();
            }
            try {
                message = inputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
                message = "IOException: " + e.toString();
            }
            return message;
        }

        @Override
        public void run() {
           // try {
                //inputStream = hostThreadSocket.getInputStream();
                //outputStream = hostThreadSocket.getOutputStream();
                ///printStream = new PrintStream(outputStream);
                message = reception();
                Log.d("@------@", "fin reception :"+message);
                Log.d("@------@", "debut envoi");
                envoi("record ok");
                Log.d("@------@", "fin envoi");
                //message = reception();
                //envoi("stop ok");


            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.response.setText(message);
                }
            });

        }

    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}