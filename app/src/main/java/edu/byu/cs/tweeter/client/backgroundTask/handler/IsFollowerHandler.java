package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class IsFollowerHandler extends Handler {

    private FollowService.FollowMainObserver observer;

    public IsFollowerHandler(FollowService.FollowMainObserver observer){
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
        if (success) {
            observer.isFollowerSuccess(msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY));
        } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
            observer.isFollowerFail("Failed to determine following relationship: " + message);
        } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
            observer.isFollowerException("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }
}
