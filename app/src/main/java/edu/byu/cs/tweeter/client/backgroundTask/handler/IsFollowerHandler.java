package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;


public class IsFollowerHandler extends SimpleTaskHandler<FollowInteractionObserver> {

    public IsFollowerHandler(FollowInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteractionObserver observer, Bundle data) {
        observer.handleIsFollower(data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY));
    }
}
