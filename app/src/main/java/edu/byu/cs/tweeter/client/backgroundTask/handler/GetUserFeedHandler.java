package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;

public class GetUserFeedHandler extends SimpleTaskHandler<HolderAdapterObserver> {
    public GetUserFeedHandler(HolderAdapterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(HolderAdapterObserver observer, Bundle data) {
        observer.handleHolderSuccess(data.getSerializable(GetUserTask.USER_KEY));
    }
}
