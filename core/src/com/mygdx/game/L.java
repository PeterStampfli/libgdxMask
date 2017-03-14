package com.mygdx.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by peter on 11/19/16.
 */

public class L {
    /**
     * debug message
     * @param message a text (String)
     */
    public static void log(String message){ Gdx.app.log("L.log",message);}

    /**
     * debug message
     * @param n an integer number(long, int or short)
     */
    public static void log(long n){ log(""+n);}

    /**
     * quick and dirty way for printing debug messages
     * @param f floating point number (double or float)
     */
    public static void log(double f){  log(""+f);}

    /**
     * quick and dirty way for printing debug messages
     * @param b boolean
     */
    public static void log(boolean b){  log(""+b);}


}
