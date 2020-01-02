package com.itijavafinalprojectteam8.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Client {
    public Socket mSocket = null;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;

    public final void init() throws IOException {
        if (mSocket == null)
            throw new NullPointerException("Socket is NULL!");

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
}
