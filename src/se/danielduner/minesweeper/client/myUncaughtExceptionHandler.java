package se.danielduner.minesweeper.client;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

public class myUncaughtExceptionHandler implements UncaughtExceptionHandler {
    
    @Override
    public void onUncaughtException (Throwable e) {
        e.printStackTrace();
    }
}
