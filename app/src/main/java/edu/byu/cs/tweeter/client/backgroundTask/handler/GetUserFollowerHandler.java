package edu.byu.cs.tweeter.client.backgroundTask.handler;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserFollowerHandler extends Handler {
    private FollowService.FollowersObserver observer;

    public GetUserFollowerHandler(FollowService.FollowersObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
        if (success) {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
            observer.FollowerHolderSuccess(user);
        } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
            observer.FollowerHolderFail("Failed to get user's profile: " + message);
        } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
            observer.FollowerHolderException("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }
}
