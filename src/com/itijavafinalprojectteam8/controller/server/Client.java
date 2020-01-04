package com.itijavafinalprojectteam8.controller.server;

import com.itijavafinalprojectteam8.model.Player;
import com.sun.istack.internal.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class Client extends Thread {

    private Socket mSocket = null;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;
    private Player mPlayer;

    public final void init(@NotNull Socket socket) throws IOException {
        if (socket == null)
            throw new NullPointerException("Socket is NULL!");

        mSocket = socket;
        mDataInputStream = new DataInputStream(mSocket.getInputStream());
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
    }

    public final void sent(String msg) throws IOException {
        if (mDataOutputStream == null)
            throw new IllegalStateException("Can not send msg!");

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
        // TODO: 1/4/20  
    }
}
