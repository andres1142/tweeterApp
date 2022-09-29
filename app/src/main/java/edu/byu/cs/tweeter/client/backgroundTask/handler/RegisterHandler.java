package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends SimpleTaskHandler<UserInteractionObserver> {

    public RegisterHandler(UserInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserInteractionObserver observer, Bundle data) {
        boolean success = data.getBoolean(RegisterTask.SUCCESS_KEY);
        if (success) {
            User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            try {
                observer.handleUserInteractionSuccess(registeredUser,authToken);
            } catch (Exception e) {
                observer.handleExceptionAndFail(e.getMessage());
            }
        }
    }
}