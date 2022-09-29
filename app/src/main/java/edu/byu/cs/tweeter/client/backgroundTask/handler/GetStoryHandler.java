package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.model.domain.Status;


public class GetStoryHandler extends CustomExceptionFailHandler<HolderAdapterObserver> {

    public GetStoryHandler(HolderAdapterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(HolderAdapterObserver observer, Bundle data) {
        observer.handleGetInfoSuccess((List<Status>)data.getSerializable(GetStoryTask.ITEMS_KEY),
                data.getBoolean(GetStoryTask.MORE_PAGES_KEY));
    }

    @Override
    protected void handleFailureException(HolderAdapterObserver observer, String message) {
        observer.handleGetInfoFailException(message);
    }
}

