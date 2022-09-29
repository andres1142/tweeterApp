package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;


public class LogoutHandler extends SimpleTaskHandler<UserInteractionObserver> {

    public LogoutHandler(UserInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserInteractionObserver observer, Bundle data) {
        observer.handleUserInteractionSuccess(null,null);
    }
}