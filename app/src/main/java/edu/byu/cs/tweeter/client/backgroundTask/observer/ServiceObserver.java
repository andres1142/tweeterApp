package edu.byu.cs.tweeter.client.backgroundTask.observer;

public interface ServiceObserver {
    void handleExceptionAndFail(String message);
}
