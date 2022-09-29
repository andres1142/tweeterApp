package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;

import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public  class LoginHandler extends SimpleTaskHandler<UserInteractionObserver> {


    public LoginHandler(UserInteractionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserInteractionObserver observer, Bundle data) {
        Cache.getInstance().setCurrUser((User) data.getSerializable(LoginTask.USER_KEY));
        Cache.getInstance().setCurrUserAuthToken((AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY));

        observer.handleUserInteractionSuccess((User) data.getSerializable(LoginTask.USER_KEY),
                (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY));
    }
}
