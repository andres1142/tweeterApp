package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;

import edu.byu.cs.tweeter.model.domain.User;

public class GetUserFollowingHandler extends SimpleTaskHandler<HolderAdapterObserver> {

    public GetUserFollowingHandler(HolderAdapterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(HolderAdapterObserver observer, Bundle data) {
        observer.handleHolderSuccess((User) data.getSerializable(GetUserTask.USER_KEY));
    }
}