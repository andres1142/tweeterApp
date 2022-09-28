package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedHandler extends Handler {
    private StatusService.FeedObserver observer;

    public GetFeedHandler(StatusService.FeedObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
        if (success) {
            List<Status> stories = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.GetFeedSuccess(stories, hasMorePages);
        } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
            observer.GetFeedFail("Failed to get feed: " + message);
        } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
            observer.GetFeedException("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}