package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.RegisterHandler;
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

    public interface LogOutObserver {
        void logOutSuccess();

        void logOutFail(String message);

        void logOutException(String message);
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

    public void LogOut(LogOutObserver observer){
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
}