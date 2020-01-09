package com.itijavafinalprojectteam8.controller.server;

import com.itijavafinalprojectteam8.model.Player;
import com.sun.istack.internal.NotNull;

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

    public final void init(@NotNull Socket socket) throws IOException {
        if (socket == null)
            throw new NullPointerException("Socket is NULL!");

        mSocket = socket;
        mDataInputStream = new DataInputStream(mSocket.getInputStream());
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
    }

    public final void send(String msg) throws IOException {
        if (mDataOutputStream == null)
            throw new IllegalStateException("Can not send msg!");

        System.out.println("[send] attempt to send to server");
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
                System.out.println("Attempt to read from client");
                String msg = read();
                if (!msg.isEmpty()) {

                    //testing
                    System.out.println("client sent: " + msg);
                    send(msg);
                }
                Logger.getLogger("SERVER").log(Level.INFO, "");
//                System.out.println("Attempt to wait before reading again");
//                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("CLIENT THREAD JOB ENDED!!!!");
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
