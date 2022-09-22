package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LogInPresenter implements UserService.LogInObserver {

    //Methods that the presenter can call on the view(the contract)
    public interface LogInView {
        void displayErrorMessage(String message);

        void clearErrorMessage();

        void displayInfoMessage(String message);

        void clearInfoMessage();

        void navigateToUser(User user);
    }

    private LogInView view;

    public LogInPresenter(LogInView view) {
        this.view = view;
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

    //Methods that the view can call
    public void LogIn(String username, String password) {
        String errorMessage = validateLogin(username, password);
        if (errorMessage == null) {
            view.clearErrorMessage();
            view.displayInfoMessage("Logging In...");
            new UserService().LogIn(username, password, this);
        } else {
            view.displayErrorMessage(errorMessage);
        }
    }


    //The methods related to observing the model layer
    @Override
    public void handleLogInSuccess(User user, AuthToken authToken) {
        view.clearInfoMessage();
        view.clearErrorMessage();

        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user);
    }

    @Override
    public void handleLogInFail(String message) {
        view.displayInfoMessage("Failed to login: " + message);
    }

    @Override
    public void handleLogInThrewException(Exception e) {
        view.displayErrorMessage("Failed to login because of exception: " + e.getMessage());
    }
}
