package com.vividprojects.protoplanner.Widgets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.kornilov on 01.02.2018.
 */

public class Pallet {
    public final static int RED             = 0xFFF44336;
    public final static int PINK            = 0xFFE91E63;
    public final static int PURPLE          = 0xFF9C27B0;
    public final static int DEEP_PURPLE     = 0xFF673AB7;
    public final static int INDIGO          = 0xFF3F51B5;
    public final static int BLUE            = 0xFF2196F3;
    public final static int LIGHT_BLUE      = 0xFF03A9F4;
    public final static int CYAN            = 0xFF00BCD4;
    public final static int TEAL            = 0xFF009688;
    public final static int GREEN           = 0xFF4CAF50;
    public final static int LIGHT_GREEN     = 0xFF8BC34A;
    public final static int LIME            = 0xFFCDDC39;
    public final static int YELLOW          = 0xFFFFEB3B;
    public final static int AMBER           = 0xFFFFC107;
    public final static int ORANGE          = 0xFFFF9800;
    public final static int DEEP_ORANGE     = 0xFFFF5722;
    public final static int BROWN           = 0xFF795548;
    public final static int GREY            = 0xFF9E9E9E;
    public final static int BLUE_GRAY       = 0xFF607D8B;
    public final static int BLACK           = 0xFF000000;
    public final static int tRED            = 0xFFFFFFFF;
    public final static int tPINK           = 0xFFFFFFFF;
    public final static int tPURPLE         = 0xFFFFFFFF;
    public final static int tDEEP_PURPLE    = 0xFFFFFFFF;
    public final static int tINDIGO         = 0xFFFFFFFF;
    public final static int tBLUE           = 0xFFFFFFFF;
    public final static int tLIGHT_BLUE     = 0xFFFFFFFF;
    public final static int tCYAN           = 0xFFFFFFFF;
    public final static int tTEAL           = 0xFFFFFFFF;
    public final static int tGREEN          = 0xFFFFFFFF;
    public final static int tLIGHT_GREEN    = 0xFFFFFFFF;
    public final static int tLIME           = 0xFF757575;
    public final static int tYELLOW         = 0xFF757575;
    public final static int tAMBER          = 0xFF757575;
    public final static int tORANGE         = 0xFFFFFFFF;
    public final static int tDEEP_ORANGE    = 0xFFFFFFFF;
    public final static int tBROWN          = 0xFFFFFFFF;
    public final static int tGREY           = 0xFFFFFFFF;
    public final static int tBLUE_GRAY      = 0xFFFFFFFF;
    public final static int tBLACK          = 0xFFFFFFFF;

    public static List<Integer> getColors(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(RED);
        list.add(PINK);
        list.add(PURPLE);
        list.add(DEEP_PURPLE);
        list.add(INDIGO);
        list.add(BLUE);
        list.add(LIGHT_BLUE);
        list.add(CYAN);
        list.add(TEAL);
        list.add(GREEN);
        list.add(LIGHT_GREEN);
        list.add(LIME);
        list.add(YELLOW);
        list.add(AMBER);
        list.add(ORANGE);
        list.add(DEEP_ORANGE);
        list.add(BROWN);
        list.add(GREY);
        list.add(BLUE_GRAY);
        list.add(BLACK);
        return list;
    }

    public static List<Integer> getTextColors(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(tRED);
        list.add(tPINK);
        list.add(tPURPLE);
        list.add(tDEEP_PURPLE);
        list.add(tINDIGO);
        list.add(tBLUE);
        list.add(tLIGHT_BLUE);
        list.add(tCYAN);
        list.add(tTEAL);
        list.add(tGREEN);
        list.add(tLIGHT_GREEN);
        list.add(tLIME);
        list.add(tYELLOW);
        list.add(tAMBER);
        list.add(tORANGE);
        list.add(tDEEP_ORANGE);
        list.add(tBROWN);
        list.add(tGREY);
        list.add(tBLUE_GRAY);
        list.add(tBLACK);
        return list;
    }

    public static List<String> getNameColors(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Red");
        list.add("Pink");
        list.add("Purple");
        list.add("Deep purple");
        list.add("Indigo");
        list.add("Blue");
        list.add("Light blue");
        list.add("Cyan");
        list.add("Teal");
        list.add("Green");
        list.add("Light green");
        list.add("Lime");
        list.add("Yellow");
        list.add("Amber");
        list.add("Orange");
        list.add("Deep orange");
        list.add("Brown");
        list.add("Gray");
        list.add("Blue gray");
        list.add("Black");
        return list;
    }

    public static int getTextColor(int color){
        int textColor;
        switch (color) {
            case RED:
                textColor = tRED;
                break;
            case PINK:
                textColor = tPINK;
                break;
            case PURPLE:
                textColor = tPURPLE;
                break;
            case DEEP_PURPLE:
                textColor = tDEEP_PURPLE;
                break;
            case INDIGO:
                textColor = tINDIGO;
                break;
            case BLUE:
                textColor = tBLUE;
                break;
            case LIGHT_BLUE:
                textColor = tLIGHT_BLUE;
                break;
            case CYAN:
                textColor = tCYAN;
                break;
            case TEAL:
                textColor = tTEAL;
                break;
            case GREEN:
                textColor = tGREEN;
                break;
            case LIGHT_GREEN:
                textColor = tLIGHT_GREEN;
                break;
            case LIME:
                textColor = tLIME;
                break;
            case YELLOW:
                textColor = tYELLOW;
                break;
            case AMBER:
                textColor = tAMBER;
                break;
            case ORANGE:
                textColor = tORANGE;
                break;
            case DEEP_ORANGE:
                textColor = tDEEP_ORANGE;
                break;
            case BROWN:
                textColor = tBROWN;
                break;
            case GREY:
                textColor = tGREY;
                break;
            case BLUE_GRAY:
                textColor = tBLUE_GRAY;
                break;
            case BLACK:
                textColor = tBLACK;
                break;
            default:
                textColor = tRED;
        }

        return textColor;
    }
}
