package com.vitting.rcpsudoku.jfc.utils;

import com.vitting.rcpsudoku.jfc.config.BuildConfig;

public class DebugLogger implements Logger {

    private static DebugLogger instance;
    private BuildConfig config;

    public DebugLogger(BuildConfig config) {
        this.config = config;
    }

    public static DebugLogger getInstance(BuildConfig config) {
        if (instance == null)
            instance = new DebugLogger(config);
        return instance;
    }

    @Override
    public void logEvent(String message) {
        if (config.getIsDebug())
            System.out.println(message);
    }
}
