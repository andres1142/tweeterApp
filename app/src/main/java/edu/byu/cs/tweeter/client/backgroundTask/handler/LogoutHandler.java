package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class LogoutHandler extends Handler {
    private UserService.LogOutObserver observer;

    public LogoutHandler(UserService.LogOutObserver observer){
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
        if (success) {
            observer.logOutSuccess();
        } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
            observer.logOutFail("Failed to logout: " + message);
        } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
            observer.logOutException("Failed to logout because of exception: " + ex.getMessage());
        }
    }
}