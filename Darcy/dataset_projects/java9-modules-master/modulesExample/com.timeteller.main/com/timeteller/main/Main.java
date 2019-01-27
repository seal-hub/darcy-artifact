package com.timeteller.main;

import com.timeteller.clock.SpeakingClock;

public class Main {

    public static void main (String[] args) {
        SpeakingClock clock = new SpeakingClock();

        clock.tellTheTime();
    }
}
