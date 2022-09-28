package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowersCountHandler extends Handler {
    private FollowService.FollowMainObserver observer;

    public GetFollowersCountHandler(FollowService.FollowMainObserver observer){
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
        if (success) {
            observer.getFollowersCountSuccess(msg.getData().getInt(GetFollowersCountTask.COUNT_KEY));
        } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
            observer.getFollowersCountFail("Failed to get followers count: " + message);
        } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
            observer.getFollowersCountException("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }
}
