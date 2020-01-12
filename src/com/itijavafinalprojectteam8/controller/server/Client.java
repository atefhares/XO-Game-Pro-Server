package com.itijavafinalprojectteam8.controller.server;

import com.itijavafinalprojectteam8.controller.operations.Props;
import com.itijavafinalprojectteam8.controller.operations.json.JsonOperations;
import com.itijavafinalprojectteam8.controller.operations.log.GuiLogger;
import com.itijavafinalprojectteam8.model.Player;
import com.itijavafinalprojectteam8.others.Constants;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Client extends Thread {

    private Socket mSocket = null;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;
    private Player mPlayer;
    private AtomicBoolean mIsShutdownCalled = new AtomicBoolean(false);

    public final void init( Socket socket) throws IOException {
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
                GuiLogger.log("Attempt to read from client");
                String msg = read();
                if (!msg.isEmpty()) {
                    handleMessageFromClient(msg);
                }
                Logger.getLogger("SERVER").log(Level.INFO, "");
//                GuiLogger.log("Attempt to wait before reading again");
//                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GuiLogger.log("CLIENT THREAD JOB ENDED!!!!");
    }

    private void handleMessageFromClient(String jsonStr) {
        String requestType = JsonOperations.getRequestType(jsonStr);
        switch (requestType){
            case Constants.ConnectionTypes.TYPE_GET_ALL_PLAYERS:
                handleGetAllUsersRequest(jsonStr);
                break;
        }
    }

    private void handleGetAllUsersRequest(String jsonStr) {
        try {
            send(Props.allPlayersJson.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (mDataInputStream != null) {
            try {
                mDataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mDataOutputStream != null) {
            try {
                mDataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mIsShutdownCalled.set(true);

    }

    @Override
    public synchronized void start() {

        //initial code before starting the thread
        mIsShutdownCalled.set(false);

        //start the thread
        super.start();
    }
}
