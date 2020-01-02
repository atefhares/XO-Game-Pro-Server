package com.itijavafinalprojectteam8.controller;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * GameServer contains all server related operations
 *
 * @author ahares
 */
public class GameServer {

    public static final int SERVER_PORT = 8000;
    private static ServerSocket mGameConnectionsSocket;

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

    }
}
