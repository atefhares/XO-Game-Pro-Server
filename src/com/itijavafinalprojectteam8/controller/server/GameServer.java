package com.itijavafinalprojectteam8.controller;


import com.itijavafinalprojectteam8.controller.JsonOperations.JsonParser;
import com.itijavafinalprojectteam8.others.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * GameServer contains all server related operations
 *
 * @author ahares
 */
public class GameServer {

    public static final int SERVER_PORT = 8000;
    private static ServerSocket mGameConnectionsSocket;
    private static Thread mMainServiceThread;

    public static void startServer() {
        if (mGameConnectionsSocket != null && !mGameConnectionsSocket.isClosed()) {
            System.out.println("Server already running...");
            return;
        }

        try {
            mGameConnectionsSocket = new ServerSocket(SERVER_PORT);

            startServerMainService();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        if (mGameConnectionsSocket != null && mGameConnectionsSocket.isClosed()) {
            System.out.println("Server is NOT running...");
            return;
        }
        try {
            if (mGameConnectionsSocket != null) {
                mGameConnectionsSocket.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for creating the JOB or Service
     * that runs in Bg and waits for clients to connect
     */
    private static void startServerMainService() {
        mMainServiceThread = new Thread(() -> {
            while (mGameConnectionsSocket != null && !mGameConnectionsSocket.isClosed()) {
                try {
                    Socket clientSocket = mGameConnectionsSocket.accept();
                    handleClient(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mMainServiceThread.start();

    }

    private static void handleClient(Socket clientSocket) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        String connectionJsonArg = dataInputStream.readUTF();
        String requestType = JsonParser.getRequestType(connectionJsonArg);
        switch (requestType){
            case Constants.ConnectionTypes.TYPE_SIGN_IN:
                handleSignInRequest(clientSocket, connectionJsonArg);
                break;

            case Constants.ConnectionTypes.TYPE_SIGN_UP:
                handleSignUpRequest(clientSocket, connectionJsonArg);
                break;
        }
    }

    private static void handleSignUpRequest(Socket clientSocket, String connectionJsonArg) {

    }

    private static void handleSignInRequest(Socket clientSocket, String connectionJsonArg) {

    }
}
