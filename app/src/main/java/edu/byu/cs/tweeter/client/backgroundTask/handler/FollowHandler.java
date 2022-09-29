package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;

public class FollowHandler extends CustomExceptionFailHandler<FollowInteractionObserver> {

    public FollowHandler(FollowInteractionObserver observer){
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteractionObserver observer, Bundle data) {
        observer.handleSuccessFollowUnfollow(false);
    }

    @Override
    protected void handleFailureException(FollowInteractionObserver observer, String message) {
        observer.ExceptionFailFollowUnfollow(message);
    }
}
