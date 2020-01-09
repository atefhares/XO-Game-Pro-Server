package com.itijavafinalprojectteam8.controller.server;


import com.itijavafinalprojectteam8.controller.operations.json.JsonOperations;
import com.itijavafinalprojectteam8.controller.operations.database.DatabaseHelper;
import com.itijavafinalprojectteam8.model.Player;
import com.itijavafinalprojectteam8.others.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GameServer contains all server related operations
 *
 * @author ahares
 */
public class GameServer {

    public static final int SERVER_PORT = 8000;
    private static ServerSocket mGameConnectionsSocket;
    private static Thread mMainServiceThread;
    private static ConcurrentHashMap<String, Client> allClients = new ConcurrentHashMap<>();

    public static void startServer() {
        if (mGameConnectionsSocket != null && !mGameConnectionsSocket.isClosed()) {
            System.out.println("Server already running...");
            return;
        }

        try {
            mGameConnectionsSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server socket created...");

            System.out.println("attempt to start main server service...");
            startServerMainService();

            System.out.println("attempt to open connection to database...");
            DatabaseHelper.setupDatabaseConnection();

            System.out.println("attempt to create tables IF NOT EXISTS...");
            DatabaseHelper.createTables();
        } catch (IOException | SQLException e) {
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
                    System.out.println("attempt to accept clients...");
                    Socket clientSocket = mGameConnectionsSocket.accept();
                    System.out.println("accepted new client: " + clientSocket.toString() + "\nattempt to handle it...");
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
        System.out.println("[handleClient] connection json: " + connectionJsonArg);
        String requestType = JsonOperations.getRequestType(connectionJsonArg);
        System.out.println("[handleClient] requestType: " + requestType);

        switch (requestType) {
            case Constants.ConnectionTypes.TYPE_SIGN_IN:
                handleSignInRequest(clientSocket, connectionJsonArg);
                break;

            case Constants.ConnectionTypes.TYPE_SIGN_UP:
                handleSignUpRequest(clientSocket, connectionJsonArg);
                break;
        }
    }

    private static void handleSignUpRequest(Socket clientSocket, String connectionJsonArg) {
        try {
            Vector<String> data = JsonOperations.getSignUpData(connectionJsonArg);
            Client client = new Client();
            client.init(clientSocket);

            if (DatabaseHelper.playerAlreadyRegistered(data.get(1))) {
                System.out.println("[handleSignUpRequest] Player already registered!");
                client.send(JsonOperations.getSignUpErrorResponse("Already registered!"));
                client.shutdown();
                return;
            }


            DatabaseHelper.insertPlayer(data);
            Player player = DatabaseHelper.getPlayerByEmail(data.get(1));
            client.setPlayer(player);
            client.start();
            allClients.put(player.email, client);
            client.send(JsonOperations.getSignUpConfirmationResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleSignInRequest(Socket clientSocket, String connectionJsonArg) {
        try {
            System.out.println("[handleSignInRequest] called");

            Vector<String> data = JsonOperations.getSignInData(connectionJsonArg);
            System.out.println("[handleSignInRequest] data: " + data);

            Client client = new Client();
            client.init(clientSocket);

            if (DatabaseHelper.isUserCredentialsCorrect(data.get(0), data.get(1))) {
                Player player = DatabaseHelper.getPlayerByEmail(data.get(0));
                System.out.println("created player: " + player.toString());
                client.setPlayer(player);
                client.start();
                allClients.put(player.email, client);
                client.send(JsonOperations.getSignInConfirmationResponse());
            } else {
                client.send(JsonOperations.getSignInErrorResponse("Wrong user name or pass"));
                client.shutdown();
                client = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
