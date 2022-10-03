package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.View.RegisterLogInPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserInteractionPresenter<T extends RegisterLogInPresenter> implements UserInteractionObserver {

    private T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public UserInteractionPresenter(T view) {
        this.view = view;
    }
    @Override
    public void handlePostSuccess() {}

    @Override
    public void handleUserInteractionSuccess(User user, AuthToken authToken) {
        view.clearInfoMessage();
        view.clearErrorMessage();

        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user, authToken);
    }

    @Override
    public void handleExceptionAndFail(String message) {
        view.displayInfoMessage("Failed to complete task: " + message);
    }
}
