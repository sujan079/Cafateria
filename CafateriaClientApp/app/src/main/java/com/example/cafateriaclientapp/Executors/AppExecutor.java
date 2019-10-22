package com.example.cafateriaclientapp.Executors;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static AppExecutor INSTANCE;
    private static final Object LOCK = new Object();
    private final Executor diskIO;


    public AppExecutor(Executor diskIO) {
        this.diskIO = diskIO;

    }

    public static AppExecutor getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return INSTANCE;
    }

    public Executor getDiskIO() {
        return diskIO;
    }


}
