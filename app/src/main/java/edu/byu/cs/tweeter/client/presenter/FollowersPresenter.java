package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter implements FollowService.FollowersObserver {

    private FollowersView view;

    public interface FollowersView{
        void setLoading(boolean value);
        void addItems(List<User> newUsers);
        void updateInfoView(User user);
        void displayInfoMessage(String message);
    }

    public FollowersPresenter(FollowersView view){
        this.view = view;
    }

    public void FollowersHolder(String username){
        view.displayInfoMessage("Getting user's profile...");
        new FollowService().FollowersHolder(username, this);
    }


    @Override
    public void FollowerHolderSuccess(User user) {
        view.updateInfoView(user);
    }

    @Override
    public void FollowerHolderFail(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void FollowerHolderException(String message) {
        view.displayInfoMessage(message);
    }
}