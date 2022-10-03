package edu.byu.cs.tweeter.client.presenter.View;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface RegisterLogInPresenter extends  UserInteractionView{

    void clearErrorMessage();

    void displayImageNotFound();

    void navigateToUser(User user, AuthToken token);
}
