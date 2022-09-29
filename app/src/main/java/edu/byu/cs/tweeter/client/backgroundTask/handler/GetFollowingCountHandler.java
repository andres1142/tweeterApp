package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;


public class GetFollowingCountHandler extends SimpleTaskHandler<FollowInteractionObserver> {

    public GetFollowingCountHandler(FollowInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteractionObserver observer, Bundle data) {
        observer.handleFollowingCount(data.getInt(GetCountTask.COUNT_KEY));
    }
}
