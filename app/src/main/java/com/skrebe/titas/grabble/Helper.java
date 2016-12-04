package com.skrebe.titas.grabble;

import java.util.Calendar;

public class Helper {

    public static int wordScore(String word){
        word = word.toLowerCase().trim();
        int score = 0;
        for(int i = 0; i < word.length(); i++){
            score += letterScore(word.charAt(i));
        }
        return score;
    }

    private static int letterScore(char c) {
        switch (c){
            case 'a':
                return 3;
            case 'b':
                return 20;
            case 'c':
                return 13;
            case 'd':
                return 10;
            case 'e':
                return 1;
            case 'f':
                return 15;
            case 'g':
                return 18;
            case 'h':
                return 9;
            case 'i':
                return 5;
            case 'j':
                return 25;
            case 'k':
                return 22;
            case 'l':
                return 11;
            case 'm':
                return 14;
            case 'n':
                return 6;
            case 'o':
                return 4;
            case 'p':
                return 19;
            case 'r':
                return 8;
            case 's':
                return 7;
            case 't':
                return 2;
            case 'u':
                return 12;
            case 'v':
                return 21;
            case 'w':
                return 17;
            case 'x':
                return 23;
            case 'y':
                return 16;
            case 'z':
                return 26;
            default:
                return -26*8;
        }
    }

    public static String getDayInString(int day) {
        switch (day){
            case Calendar.MONDAY:
                return "monday";
            case Calendar.TUESDAY:
                return "tuesday";
            case Calendar.WEDNESDAY:
                return "wednesday";
            case Calendar.THURSDAY:
                return "thursday";
            case Calendar.FRIDAY:
                return "friday";
            case Calendar.SATURDAY:
                return "saturday";
            case Calendar.SUNDAY:
                return "sunday";
            default:
                return "unknown";
        }
    }

}
