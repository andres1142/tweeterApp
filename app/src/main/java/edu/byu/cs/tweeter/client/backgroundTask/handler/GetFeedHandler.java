package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;


import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedHandler extends CustomExceptionFailHandler<HolderAdapterObserver> {

    public GetFeedHandler(HolderAdapterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(HolderAdapterObserver observer, Bundle data) {
        observer.handleGetInfoSuccess((List<Status>)data.getSerializable(GetFeedTask.ITEMS_KEY),
                data.getBoolean(GetFeedTask.MORE_PAGES_KEY));
    }

    @Override
    protected void handleFailureException(HolderAdapterObserver observer, String message) {
        observer.handleGetInfoFailException(message);
    }
}