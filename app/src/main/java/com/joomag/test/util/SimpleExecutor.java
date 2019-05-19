package com.joomag.test.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executor class provides the different executors e.g UI, DB, NETWORK
 */
public class SimpleExecutor {
    private static SimpleExecutor simpleExecutor;
    private Executor networkExecutor;
    private Executor dbExecutor;
    private Executor uiExecutor;

    public static SimpleExecutor getInstance() {
        if (simpleExecutor == null) {
            synchronized (SimpleExecutor.class) {
                if (simpleExecutor == null) {
                    simpleExecutor = new SimpleExecutor();
                }
            }
        }
        return simpleExecutor;
    }

    private SimpleExecutor() {
        networkExecutor = Executors.newFixedThreadPool(20);
        dbExecutor = Executors.newFixedThreadPool(20);
        uiExecutor = new MainThreadExecutor();
    }

    public void lunchOn(LunchOn lunchOn, Runnable runnable) {
        switch (lunchOn) {
            case NETWORK:
                networkExecutor.execute(runnable);
                break;
            case UI:
                uiExecutor.execute(runnable);
                break;
            case DB:
                dbExecutor.execute(runnable);
                break;
        }
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public enum LunchOn {
        NETWORK,
        DB,
        UI
    }
}
