package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class FollowHandler extends Handler {
    private FollowService.FollowMainObserver observer;

    public FollowHandler(FollowService.FollowMainObserver observer){
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
        if (success) {
            observer.followUserSuccess(false);
        } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
            observer.followUserFail("Failed to unfollow: " + message);
        } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
            observer.followUserExceptionException("Failed to unfollow because of exception: " + ex.getMessage());
        }
    }
}
