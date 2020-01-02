package com.itijavafinalprojectteam8.controller;

import com.itijavafinalprojectteam8.model.GamePlay;
import com.itijavafinalprojectteam8.model.HostClient;
import com.itijavafinalprojectteam8.model.JoinerClient;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * GameServer contains all server related operations
 *
 * @author ahares
 */
public class GameServer {

    public static final int SERVER_PORT = 8000;
    private static ServerSocket mGameConnectionsSocket;
    private static Thread mMainServiceThread;

    private static Vector<GamePlay> allGamePlays = new Vector<>();

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

    }

    private static void handleClient(Socket clientSocket) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        String connectionArg = dataInputStream.readUTF();

        // check connection type  if connecting after
        // SignUp or after SignIn
        // if SignUP


//        boolean isHost = checkIfHost(connectionArg);
//        String hostIp = getHostIp(connectionArg);
//
//        if (isHost) {
//            handleHostClient(clientSocket, hostIp);
//        } else {
//            GamePlay gamePlay = handleJoinerClient(clientSocket, hostIp);
//            if (gamePlay != null) {
//                gamePlay.startGame();
//            }
//        }
    }

    private static void handleHostClient(Socket clientSocket, String hostIp) {
        GamePlay gamePlay = new GamePlay();
        gamePlay.mHostClient = new HostClient();
        gamePlay.mHostClient.mSocket = clientSocket;
        gamePlay.mHostClient.ip = hostIp;
        allGamePlays.add(gamePlay);
    }

    private static GamePlay handleJoinerClient(Socket clientSocket, String hostIp) {
        for (GamePlay gamePlay : allGamePlays) {
            if (gamePlay.mHostClient.ip.equals(hostIp)) {
                JoinerClient joinerClient = new JoinerClient();
                joinerClient.mSocket = clientSocket;
                gamePlay.mJoinerClient = joinerClient;
                return gamePlay;
            }
        }
        return null;
    }

    private static String getHostIp(String connectionArg) {
        return null;
    }

    private static boolean checkIfHost(String connectionArg) {
        JSONParser parser = new JSONParser(connectionArg);
        return false;
    }
}
