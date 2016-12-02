package com.system.androidpigbank.controllers.helpers;

public enum Quinzena {
    PRIMEIRA, SEGUNDA;

    public int getId() {
        return ordinal() +1;
    }

    public static Quinzena getEnum(int position) {
        return Quinzena.values()[position -1];
    }
}