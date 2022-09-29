package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;

public class PostStatusHandler extends SimpleTaskHandler<UserInteractionObserver> {

    public PostStatusHandler(UserInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserInteractionObserver observer, Bundle data) {
        observer.handlePostSuccess();
    }
}

