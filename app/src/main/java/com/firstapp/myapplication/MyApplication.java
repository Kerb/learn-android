package com.firstapp.myapplication;

import android.app.Application;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Main app classs
 */
public class MyApplication extends Application {

    static {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.i("\n" +
            "ЗАПУСКАЕМ \n" +
            "░ГУСЯ░▄▀▀▀▄░РАБОТЯГИ░░ \n" +
            "▄███▀░◐░░░▌░░░░░░░ \n" +
            "░░░░▌░░░░░▐░░░░░░░ \n" +
            "░░░░▐░░░░░▐░░░░░░░ \n" +
            "░░░░▌░░░░░▐▄▄░░░░░ \n" +
            "░░░░▌░░░░▄▀▒▒▀▀▀▀▄ \n" +
            "░░░▐░░░░▐▒▒▒▒▒▒▒▒▀▀▄ \n" +
            "░░░▐░░░░▐▄▒▒▒▒▒▒▒▒▒▒▀▄ \n" +
            "░░░░▀▄░░░░▀▄▒▒▒▒▒▒▒▒▒▒▀▄ \n" +
            "░░░░░░▀▄▄▄▄▄█▄▄▄▄▄▄▄▄▄▄▄▀▄ \n" +
            "░░░░░░░░░░░▌▌░▌▌░░░░░ \n" +
            "░░░░░░░░░░░▌▌░▌▌░░░░░ \n" +
            "░░░░░░░░░▄▄▌▌▄▌▌░░░░░");
    }
}
