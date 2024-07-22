package com.rexchoppers.mchunt.models;

public class Countdown {
    private int countdown;

    public Countdown(int countdown) {
        this.countdown = countdown;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void decrementCountdown() {
        countdown--;
    }

    public boolean isCountdownFinished() {
        return countdown <= 0;
    }
}
