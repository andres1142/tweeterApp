package edu.byu.cs.tweeter.client.backgroundTask.handler;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersHandler extends Handler {
    private FollowService.FollowersObserver observer;

    public GetFollowersHandler(FollowService.FollowersObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
        if (success) {
            List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

            observer.GetFollowersSuccess(followers, hasMorePages);
        } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
            observer.GetFollowersFail("Failed to get followers: " + message);
        } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
            observer.GetFollowersException("Failed to get followers because of exception: " + ex.getMessage());
        }
    }
}