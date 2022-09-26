package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter implements FollowService.FollowersObserver {

    public static final int PAGE_SIZE = 10;

    private FollowersView view;

    private User user;
    private User lastFollower;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public interface FollowersView{
        void setLoading(boolean value);
        void addItems(List<User> newUsers);
        void updateInfoView(User user);
        void displayInfoMessage(String message);
    }

    public FollowersPresenter(User user,FollowersView view){
        this.user = user;
        this.view = view;
    }

    private void setLastFollower(User lastFollower) {
        this.lastFollower = lastFollower;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    private void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void FollowersHolder(String username){
        view.displayInfoMessage("Getting user's profile...");
        new FollowService().FollowersHolder(username, this);
    }

    public void LoadMoreItems(){
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            new FollowService().FollowersLoadMore(user, PAGE_SIZE, lastFollower, this);
        }
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

    @Override
    public void GetFollowersSuccess(List<User> followers, boolean hasMorePages) {
        setLastFollower((followers.size() > 0) ? followers.get(followers.size() - 1) : null);
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(followers);
        setLoading(false);
    }

    @Override
    public void GetFollowersFail(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }

    @Override
    public void GetFollowersException(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }
}