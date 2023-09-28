package com.packag;
import java.time.LocalDate;

public class UserOnline {
    private LocalDate startSession;
    private LocalDate endSession;

    public UserOnline(LocalDate startSession, LocalDate endSession) {
        this.startSession = startSession;
        this.endSession = endSession;
    }

    public LocalDate getStartSession() {
        return startSession;
    }

    public void setStartSession(LocalDate startSession) {
        this.startSession = startSession;
    }

    public LocalDate getEndSession() {
        return endSession;
    }

    @Override
    public String toString() {
        return "UserOnline{" +
                "startSession=" + startSession +
                ", endSession=" + endSession +
                '}';
    }

    public void setEndSession(LocalDate endSession) {
        this.endSession = endSession;
    }
}