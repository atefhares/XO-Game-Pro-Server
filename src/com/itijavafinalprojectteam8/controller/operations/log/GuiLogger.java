package com.itijavafinalprojectteam8.controller.operations.log;

import com.itijavafinalprojectteam8.interfaces.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GuiLogger {

    private static View mView;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

    public static void init(View view) {
        mView = view;
    }


    public static void log(String text) {
        if (mView != null) {
            System.out.println("[" + dtf.format(LocalDateTime.now()) + "] " + text);
            mView.displayLog("[" + dtf.format(LocalDateTime.now()) + "] " + text);
        }
    }
}
