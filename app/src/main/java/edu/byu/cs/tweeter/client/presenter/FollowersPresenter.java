package edu.byu.cs.tweeter.client.presenter;

import androidx.fragment.app.Fragment;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter implements FollowService.FollowersObserver {


    //Methods that the presenter can call on the view(the contract)
    public interface FollowersView {
        void clearInfoMessage();

        void displayInfoMessage(String message);

        void bindUserView(User user);

        void getUser(User user);
    }

    //Methods that the view can call
    private FollowersView view;

    public FollowersPresenter(FollowersView view) {
        this.view = view;
    }

    public void FollowersHolder(String username) {
        view.clearInfoMessage();
        view.displayInfoMessage("Getting user's profile...");
        new FollowService().FollowersHolder(username, this);
    }

    public void bindUserToView(User user) {
        view.bindUserView(user);
    }


    //The methods related to observing the model layer

    @Override
    public void getUserSuccess(User user) {
        view.getUser(user);
    }

    @Override
    public void getUserFail(String message) {
        view.clearInfoMessage();
        view.displayInfoMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void getUserException(Exception e) {
        view.displayInfoMessage("Failed to get user's profile because of exception: " + e.getMessage());
    }
}
