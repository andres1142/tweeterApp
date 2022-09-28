package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowingCountHandler extends Handler {
    private FollowService.FollowMainObserver observer;

    public GetFollowingCountHandler(FollowService.FollowMainObserver observer){
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
        if (success) {
            observer.getFollowingCountSuccess(msg.getData().getInt(GetFollowingCountTask.COUNT_KEY));
        } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
            observer.getFollowingCountFail("Failed to get following count: " + message);
        } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
            observer.getFollowingCountException("Failed to get following count because of exception: " + ex.getMessage());
        }
    }
}
