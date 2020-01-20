package com.itijavafinalprojectteam8.controller.server;

import com.itijavafinalprojectteam8.controller.operations.Props;
import com.itijavafinalprojectteam8.controller.operations.database.DatabaseHelper;
import com.itijavafinalprojectteam8.controller.operations.json.JsonOperations;
import com.itijavafinalprojectteam8.controller.operations.log.GuiLogger;
import com.itijavafinalprojectteam8.model.Game;
import com.itijavafinalprojectteam8.model.Player;
import com.itijavafinalprojectteam8.others.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Client extends Thread {

    private Socket mSocket = null;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;
    private Player mPlayer;
    private AtomicBoolean mIsShutdownCalled = new AtomicBoolean(false);

    public final void init(Socket socket) throws IOException {
        if (socket == null)
            throw new NullPointerException("Socket is NULL!");

        mSocket = socket;
        mDataInputStream = new DataInputStream(mSocket.getInputStream());
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());

    }

    public final void send(String msg) throws IOException {
        if (mDataOutputStream == null)
            throw new IllegalStateException("Can not send msg!");

        GuiLogger.log("[send] attempt to send to client");
        mDataOutputStream.writeUTF(msg);
    }

    public final String read() throws IOException {
        if (mDataInputStream == null)
            throw new IllegalStateException("Can not read msg!");

        return mDataInputStream.readUTF();
    }

    public void setPlayer(Player mPlayer) {
        this.mPlayer = mPlayer;
    }

    @Override
    public void run() {
        while (!mIsShutdownCalled.get()) {
            try {
                if (mSocket == null || mSocket.isClosed()) {
                    GuiLogger.log("Player: " + mPlayer.email + " is OFFLINE");
                    shutdown();
                    return;
                }

                GuiLogger.log("Attempt to read from client");
                String msg = read();
                if (!msg.isEmpty()) {
                    handleMessageFromClient(msg);
                }
                Logger.getLogger("SERVER").log(Level.INFO, "");

            } catch (Exception e) {
                e.printStackTrace();

                GuiLogger.log("Player: " + mPlayer.email + " is OFFLINE");
                shutdown();
            }
        }

        GuiLogger.log("CLIENT THREAD JOB ENDED!!!!");
    }

    private void handleMessageFromClient(String jsonStr) {
        String requestType = JsonOperations.getRequestType(jsonStr);
        switch (requestType) {
            case Constants.ConnectionTypes.TYPE_GET_ALL_PLAYERS:
                handleGetAllUsersRequest(jsonStr);
                break;

            case Constants.ConnectionTypes.TYPE_SEND_INVITATION:
                handleSendInvitationRequest(jsonStr);
                break;

            case Constants.ConnectionTypes.TYPE_INVITATION_RESULT:
                handleSendInvitationResponse(jsonStr);
                break;

            case Constants.ConnectionTypes.TYPE_GAME:
                handleGameRequest(jsonStr);
                break;

            case Constants.ConnectionTypes.TYPE_UPDATE_PLAYER_POINTS:
                handleUpdatePlayerPoints(jsonStr);
                break;

            case Constants.ConnectionTypes.TYPE_PAUSE_GAME:
                handlePauseGame(jsonStr);
                break;

            case Constants.ConnectionTypes.TYPE_GAME_OVER:
                handleGameOver(jsonStr);
                break;
        }
    }

    private void handleGameOver(String jsonStr) {
        String otherPlayerEmail = JsonOperations.getOtherPlayerEmail(jsonStr);

        try {
            DatabaseHelper.updatePlayerStatus(otherPlayerEmail, Constants.PlayerStatus.ONLINE_NOT_IN_GAME);
            DatabaseHelper.updatePlayerStatus(mPlayer.email, Constants.PlayerStatus.ONLINE_NOT_IN_GAME);
            DatabaseHelper.deleteGameIfExists(mPlayer.email, otherPlayerEmail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handlePauseGame(String jsonStr) {
        GuiLogger.log("[handlePauseGame] jsonStr: " + jsonStr);
        String otherPlayerEmail = JsonOperations.getOtherPlayerEmail(jsonStr);
        String gameState = JsonOperations.parseGameStateStr(jsonStr);
        GuiLogger.log("[handlePauseGame] gameState: " + gameState);

        Game game = new Game();
        game.player1Email = mPlayer.email;
        game.player2Email = otherPlayerEmail;
        game.gameState = gameState;

        try {
            DatabaseHelper.updatePlayerStatus(otherPlayerEmail, Constants.PlayerStatus.ONLINE_NOT_IN_GAME);
            DatabaseHelper.updatePlayerStatus(mPlayer.email, Constants.PlayerStatus.ONLINE_NOT_IN_GAME);

            DatabaseHelper.insertGame(game);
            GameServer.sendToOtherClient(otherPlayerEmail, JsonOperations.createGamePausedJson());


            GameServer.initAllPlayersJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUpdatePlayerPoints(String jsonStr) {
        try {
            DatabaseHelper.updatePlayerPoints(mPlayer.email);
            GameServer.initAllPlayersJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGameRequest(String jsonStr) {
        try {
            String otherPlayerEmail = JsonOperations.getOtherPlayerEmail(jsonStr);
            int cord = JsonOperations.getGameCord(jsonStr);
            GuiLogger.log("[handleGameRequest] otherPlayerEmail: " + otherPlayerEmail);
            GuiLogger.log("[handleGameRequest] cord: " + cord);

            GameServer.sendToOtherClient(
                    otherPlayerEmail,
                    JsonOperations.getGameCommunicationJson(mPlayer.email, cord)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSendInvitationResponse(String jsonStr) {
        try {
            String otherPlayerEmail = JsonOperations.getOtherPlayerEmail(jsonStr);
            boolean invitationResult = JsonOperations.parseInvitationResult(jsonStr);

            if (invitationResult) {
                DatabaseHelper.updatePlayerStatus(mPlayer.email, Constants.PlayerStatus.ONLINE_IN_GAME);
                DatabaseHelper.updatePlayerStatus(otherPlayerEmail, Constants.PlayerStatus.ONLINE_IN_GAME);
                GameServer.initAllPlayersJson();
            }

            GameServer.sendToOtherClient(
                    otherPlayerEmail,
                    JsonOperations.getInvitationResponseJson(mPlayer.email, invitationResult)
            );


            Game game = DatabaseHelper.getGame(mPlayer.email, otherPlayerEmail);
            if (invitationResult && game != null) {
                GuiLogger.log("game: " + game.toString());
                send(JsonOperations.createThereIsOldGameJson(otherPlayerEmail, game.gameState));
                GameServer.sendToOtherClient(
                        otherPlayerEmail,
                        JsonOperations.createThereIsOldGameJson(mPlayer.email, game.gameState)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSendInvitationRequest(String jsonStr) {
        try {
            String otherPlayerEmail = JsonOperations.getOtherPlayerEmail(jsonStr);
            int otherPlayerStatus = DatabaseHelper.getPlayerStatus(otherPlayerEmail);

            if (otherPlayerStatus == Constants.PlayerStatus.ONLINE_NOT_IN_GAME) {
                GameServer.sendToOtherClient(otherPlayerEmail, JsonOperations.getSendInvitationJson(mPlayer.email));
            } else if (otherPlayerStatus == Constants.PlayerStatus.ONLINE_IN_GAME) {
                send(JsonOperations.getSendInvitationError("player is in another game"));
            } else {
                send(JsonOperations.getSendInvitationError("player is in offline"));
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleGetAllUsersRequest(String jsonStr) {
        try {
            send(JsonOperations.createAllPlayersJsonString(Props.allPlayersJson.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (mDataInputStream != null) {
            try {
                mDataInputStream.close();
                mDataInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mDataOutputStream != null) {
            try {
                mDataOutputStream.close();
                mDataOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            DatabaseHelper.updatePlayerStatus(mPlayer.email, Constants.PlayerStatus.OFFLINE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GameServer.onSomeClientDisconnected(mPlayer.email);

        mIsShutdownCalled.set(true);

        mPlayer = null;

    }

    @Override
    public synchronized void start() {

        //initial code before starting the thread
        mIsShutdownCalled.set(false);

        //start the thread
        super.start();
    }
}
