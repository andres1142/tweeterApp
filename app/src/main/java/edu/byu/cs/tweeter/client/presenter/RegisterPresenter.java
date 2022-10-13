package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.View.RegisterLogInPresenter;
import edu.byu.cs.tweeter.client.presenter.View.UserInteractionView;
import edu.byu.cs.tweeter.client.presenter.View.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends UserInteractionPresenter implements UserInteractionObserver {


    public RegisterPresenter(RegisterLogInPresenter view) {
        super(view);
    }

    public void noImageFound() {
        getView().clearErrorMessage();
        getView().displayImageNotFound();
    }

    public String validateRegistration(String firstName, String lastName, String username,
                                       String password) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (username.length() == 0) {
            return "Alias cannot be empty.";
        }
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
    public void Register(String firstName, String lastName, String username,
                         String password, String imageToUpload) {
        String errorMessage = validateRegistration(firstName, lastName, username, password);

        if (errorMessage == null) {
            getView().clearInfoMessage();
            getView().displayInfoMessage("Registering...");
            new UserService().Register(firstName, lastName, username, password, imageToUpload, this);
        } else {
            getView().displayErrorMessage(errorMessage);
        }
    }

    @Override
    public void handlePostFail(String message) {

    }

    @Override
    public void handlePostException(String message) {

    }
}
