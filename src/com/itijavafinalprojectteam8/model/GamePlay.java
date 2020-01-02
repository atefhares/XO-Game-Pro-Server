package com.itijavafinalprojectteam8.model;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamePlay extends Thread {
    public static final String GAME_STARTED_STR = getGameStartedMessage();

    public HostClient mHostClient;
    public JoinerClient mJoinerClient;

    public AtomicBoolean mGameRunning = new AtomicBoolean(false);

    public void startGame() {
        if (mHostClient == null)
            throw new NullPointerException("Host is NULL!");

        if (mJoinerClient == null)
            throw new NullPointerException("Joiner is NULL!");

        try {
            mHostClient.init();
            mJoinerClient.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //start the game
        this.start();
    }

    public void stopGame() {
        mGameRunning.set(false);
    }

    @Override
    public void run() {
        //game started

        try {
            // send to host that game started
            mHostClient.sent(GAME_STARTED_STR);

            // send to joiner that game started
            mJoinerClient.sent(GAME_STARTED_STR);

            mGameRunning.set(true);

            //start reciving updates from the two clients and send them each others
            while (mGameRunning.get()) {
                String msgFromHost = mHostClient.read();
                String msgFromJoiner = mJoinerClient.read();

                if (msgFromHost != null && !msgFromHost.isEmpty()) {
                    mJoinerClient.sent(msgFromHost);
                }

                if (msgFromJoiner != null && !msgFromJoiner.isEmpty()) {
                    mHostClient.sent(msgFromJoiner);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getGameStartedMessage() {
        // TODO: 1/2/20
        return null;
    }
}
