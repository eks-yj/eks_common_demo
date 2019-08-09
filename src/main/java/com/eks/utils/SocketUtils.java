package com.eks.utils;

import com.eks.thread.base.SocketThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketUtils {
    public static void createSocketServer(Class<? extends SocketThread> clazz, Integer portInteger) throws Exception{
        ServerSocket serverSocket = new ServerSocket(portInteger);
        while (true) {
            Socket socket = serverSocket.accept();
            SocketThread socketThread = clazz.newInstance();
            socketThread.setSocket(socket);
            socketThread.start();
        }
    }
    public static Socket createSocketClient(Integer portInteger) throws IOException {
        return createSocketClient("127.0.0.1",portInteger);
    }
    public static Socket createSocketClient(String ipString,Integer portInteger) throws IOException {
        return new Socket(ipString, portInteger);
    }
}
