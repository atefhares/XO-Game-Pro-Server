package com.itijavafinalprojectteam8.controller.server;


import com.itijavafinalprojectteam8.controller.operations.Props;
import com.itijavafinalprojectteam8.controller.operations.database.DatabaseHelper;
import com.itijavafinalprojectteam8.controller.operations.json.JsonOperations;
import com.itijavafinalprojectteam8.controller.operations.log.GuiLogger;
import com.itijavafinalprojectteam8.model.Player;
import com.itijavafinalprojectteam8.others.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
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
            GuiLogger.log("Server already running...");
            return;
        }

        try {
            mGameConnectionsSocket = new ServerSocket(SERVER_PORT);
            GuiLogger.log("Server socket created...");
            GuiLogger.log("Server socket: " + mGameConnectionsSocket.toString());

            GuiLogger.log("attempt to start main server service...");
            startServerMainService();

            GuiLogger.log("attempt to open connection to database...");
            DatabaseHelper.setupDatabaseConnection();

            GuiLogger.log("attempt to create tables IF NOT EXISTS...");
            DatabaseHelper.createTables();

            DatabaseHelper.setAllPlayersOffline();

            //initial players json str
            initAllPlayersJson();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        if (mGameConnectionsSocket != null && mGameConnectionsSocket.isClosed()) {
            GuiLogger.log("Server is NOT running...");
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

    private static void startServerMainService() {
        mMainServiceThread = new Thread(() -> {
            while (mGameConnectionsSocket != null && !mGameConnectionsSocket.isClosed()) {
                try {
                    GuiLogger.log("attempt to accept clients...");
                    Socket clientSocket = mGameConnectionsSocket.accept();
                    GuiLogger.log("accepted new client: " + clientSocket.toString() + "\nattempt to handle it...");
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
        GuiLogger.log("[handleClient] connection json: " + connectionJsonArg);
        String requestType = JsonOperations.getRequestType(connectionJsonArg);
        GuiLogger.log("[handleClient] requestType: " + requestType);

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
                GuiLogger.log("[handleSignUpRequest] Player already registered!");
                client.send(JsonOperations.getSignUpErrorResponse("Already registered!"));
                client.shutdown();
                return;
            }


            DatabaseHelper.insertPlayer(data);
            Player player = DatabaseHelper.getPlayerByEmail(data.get(1));
            client.setPlayer(player);
            client.start();
            allClients.put(player.email, client);
            client.send(JsonOperations.getSignUpConfirmationResponse(player));
            DatabaseHelper.updatePlayerStatus(player.email, Constants.PlayerStatus.ONLINE_NOT_IN_GAME);

            handleOnNewClientConnected();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleSignInRequest(Socket clientSocket, String connectionJsonArg) {
        try {
            GuiLogger.log("[handleSignInRequest] called");

            Vector<String> data = JsonOperations.getSignInData(connectionJsonArg);
            GuiLogger.log("[handleSignInRequest] data: " + data);

            Client client = new Client();
            client.init(clientSocket);

            if (DatabaseHelper.isUserCredentialsCorrect(data.get(0), data.get(1))) {
                Player player = DatabaseHelper.getPlayerByEmail(data.get(0));
                GuiLogger.log("created player: " + player.toString());
                client.setPlayer(player);
                client.start();
                allClients.put(player.email, client);
                client.send(JsonOperations.getSignInConfirmationResponse(player));
                DatabaseHelper.updatePlayerStatus(player.email, Constants.PlayerStatus.ONLINE_NOT_IN_GAME);

                handleOnNewClientConnected();
            } else {
                client.send(JsonOperations.getSignInErrorResponse("Wrong user name or pass"));
                client.shutdown();
                client = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleOnNewClientConnected() {
        //initial players json str
        try {
            initAllPlayersJson();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initAllPlayersJson() throws SQLException {
        Props.allPlayersJson.setValue(JsonOperations.getPlayersListJson(DatabaseHelper.getAllPlayers()));
        sendAllPlayersJsonToAllConnectedClients();
    }

    private static void sendAllPlayersJsonToAllConnectedClients() {
        for (Map.Entry<String, Client> entry : allClients.entrySet()) {
            try {
                entry.getValue().send(JsonOperations.createAllPlayersJsonString(Props.allPlayersJson.getValue()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void onSomeClientDisconnected(String email) {
        try {
            allClients.remove(email);
            initAllPlayersJson();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void sendToOtherClient(String otherPlayerEmail, String msg) throws IOException, SQLException {
        allClients.get(otherPlayerEmail).send(msg);
    }
}
