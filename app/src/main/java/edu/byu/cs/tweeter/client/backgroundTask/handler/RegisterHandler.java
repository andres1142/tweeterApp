package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends Handler {
    private UserService.RegisterObserver registerObserver;

    public RegisterHandler(UserService.RegisterObserver registerObserver) {
        this.registerObserver = registerObserver;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
        if (success) {
            User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            try {
                registerObserver.handleRegisterSuccess(registeredUser, authToken);
            } catch (Exception e) {
                registerObserver.handleRegisterThrewException(e);
            }
        } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
            registerObserver.handleRegisterFail(message);
        } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
            registerObserver.handleRegisterThrewException(ex);
        }
    }
}
