package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public  class LoginHandler extends Handler {

    private UserService.LogInObserver logInObserver;

    public LoginHandler(UserService.LogInObserver logInObserver) {
        this.logInObserver = logInObserver;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
        if (success) {
            User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            logInObserver.handleLogInSuccess(loggedInUser, authToken);
        } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
            logInObserver.handleLogInFail(message);
        } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
            logInObserver.handleLogInThrewException(ex);
        }
    }
}
