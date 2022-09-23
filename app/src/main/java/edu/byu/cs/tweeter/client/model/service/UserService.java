package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.login.RegisterFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    //LogIn Observer
    public interface LogInObserver {
        void handleLogInSuccess(User user, AuthToken authToken);

        void handleLogInFail(String message);

        void handleLogInThrewException(Exception e);
    }

    //Register Observer
    public interface RegisterObserver {
        void handleRegisterSuccess(User user, AuthToken authToken);

        void handleRegisterFail(String message);

        void handleRegisterThrewException(Exception e);
    }

    public void LogIn(String password, String username, LogInObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password,
                new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void Register(String firstName, String lastName, String username, String password,
                         String imageBytesBase64, RegisterObserver registerObserver) {

        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                username, password, imageBytesBase64, new RegisterHandler(registerObserver));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    private class LoginHandler extends Handler {

        private LogInObserver logInObserver;

        public LoginHandler(LogInObserver logInObserver) {
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

    private class RegisterHandler extends Handler {
        private RegisterObserver registerObserver;

        public RegisterHandler(RegisterObserver registerObserver) {
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
}