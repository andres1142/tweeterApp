package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;

public class UnfollowHandler extends CustomExceptionFailHandler<FollowInteractionObserver> {


    public UnfollowHandler(FollowInteractionObserver observer){
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteractionObserver observer, Bundle data) {
        observer.handleSuccessFollowUnfollow(true);
    }
    @Override
    protected void handleFailureException(FollowInteractionObserver observer, String message) {
        observer.ExceptionFailFollowUnfollow(message);
    }
}
