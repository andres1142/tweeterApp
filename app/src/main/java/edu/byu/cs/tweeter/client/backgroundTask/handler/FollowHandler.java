package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteraction;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class FollowHandler extends FollowBackgroundHandler<FollowInteraction> {

    public FollowHandler(FollowService.FollowMainObserver observer){
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowInteraction observer, Bundle data) {
        observer.handleSuccessFollowUnfollow(false);
    }
}
