package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements UserService.RegisterObserver {

    //Methods that the presenter can call on the view(the contract)
    public interface RegisterView {
        void clearErrorMessage();

        void displayErrorMessage(String message);

        void displayImageNotFound();

        void clearInfoMessage();

        void displayInfoMessage(String message);

        void navigateToUser(User user, AuthToken token);
    }

    private RegisterView view;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
    }

    public void noImageFound() {
        view.clearErrorMessage();
        view.displayImageNotFound();
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
            view.clearInfoMessage();
            view.displayInfoMessage("Registering...");
            new UserService().Register(firstName, lastName, username, password, imageToUpload, this);
        } else {
            view.displayErrorMessage(errorMessage);
        }
    }


    //The methods related to observing the model layer
    @Override
    public void handleRegisterSuccess(User user, AuthToken token) {
        view.clearInfoMessage();
        view.clearErrorMessage();

        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user, token);
    }

    @Override
    public void handleRegisterFail(String message) {
        view.displayInfoMessage("Failed to register: " + message);

    }

    @Override
    public void handleRegisterThrewException(Exception e) {
        view.displayInfoMessage("Failed to register because of exception: " + e.getMessage());
    }
}
