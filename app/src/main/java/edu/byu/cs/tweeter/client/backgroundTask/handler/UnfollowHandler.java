package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class UnfollowHandler extends Handler {
    private FollowService.FollowMainObserver observer;

    public UnfollowHandler(FollowService.FollowMainObserver observer){
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
        if (success) {
            observer.unfollowUserSuccess(true);
        } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
            observer.unfollowUserFail("Failed to unfollow: " + message);
        } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
            observer.unfollowUserExceptionException("Failed to unfollow because of exception: " + ex.getMessage());
        }

    }
}
