package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

import edu.byu.cs.tweeter.client.presenter.View.RegisterLogInPresenter;
import edu.byu.cs.tweeter.client.presenter.View.UserInteractionView;
import edu.byu.cs.tweeter.client.presenter.View.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LogInPresenter extends UserInteractionPresenter implements UserInteractionObserver {


    public LogInPresenter(RegisterLogInPresenter view) {
        super(view);

    }

    public String validateLogin(String username, String password) {
        if (username.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (username.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }


    public void LogIn(String username, String password) {
        String errorMessage = validateLogin(username, password);
        if (errorMessage == null) {
            getView().clearErrorMessage();
            getView().displayInfoMessage("Logging In...");
            new UserService().LogIn(username, password, this);
        } else {
            getView().displayErrorMessage(errorMessage);
        }
    }
}
