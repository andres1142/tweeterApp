package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;


public class GetFollowersCountHandler extends SimpleTaskHandler<FollowInteractionObserver> {

    public GetFollowersCountHandler(FollowInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteractionObserver observer, Bundle data) {
        observer.handleFollowerCount(data.getInt(GetCountTask.COUNT_KEY));
    }
}
