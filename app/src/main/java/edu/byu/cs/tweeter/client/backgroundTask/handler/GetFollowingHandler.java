package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.model.domain.User;


public class GetFollowingHandler extends CustomExceptionFailHandler<HolderAdapterObserver> {

    public GetFollowingHandler(HolderAdapterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(HolderAdapterObserver observer, Bundle data) {
        observer.handleGetInfoSuccess((List<User>) data.getSerializable(GetFollowingTask.ITEMS_KEY),
                data.getBoolean(GetFollowingTask.MORE_PAGES_KEY));
    }

    @Override
    protected void handleFailureException(HolderAdapterObserver observer, String message) {
        observer.handleGetInfoFailException(message);
    }
}
