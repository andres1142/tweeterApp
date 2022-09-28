package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteraction;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;

public class FollowHandler extends BackgroundTaskHandler<FollowInteraction> {

    public FollowHandler(FollowService.FollowMainObserver observer){
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteraction observer, Bundle data) {
        observer.handleSuccessFollowUnfollow(true);
    }
}
