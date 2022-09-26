package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowService.FollowingObserver {

    public static final int PAGE_SIZE = 10;

    private final FollowingView view;
    private final User user;
    private final AuthToken authToken;

    private User lastFollowee;
    private boolean hasMorePages = true;
    private boolean isLoading = false;


    public interface FollowingView{
        void setLoading(boolean value);
        void addItems(List<User> newUsers);
        void displayInfoMessage(String message);
        void updateInfoView(User user);
    }
    public FollowingPresenter(FollowingView view, User user, AuthToken token) {
        this.view = view;
        this.user = user;
        this.authToken = token;
    }


    private void setLastFollowee(User lastFollowee) {
        this.lastFollowee = lastFollowee;
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

    public void FollowingHolder(String username){
        view.displayInfoMessage("Getting user's profile...");
        new FollowService().FollowingHolder(username, this);
    }
    public void LoadMoreItems() {
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            new FollowService().FollowingLoadMore(user, PAGE_SIZE,lastFollowee,this);
        }
    }

    @Override
    public void FollowingHolderSuccess(User user) {
        view.updateInfoView(user);
    }

    @Override
    public void FollowingHolderFail(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void FollowingHolderException(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void GetFollowingSuccess(List<User> following, boolean hasMorePages) {
        setLastFollowee((following.size() > 0) ? following.get(following.size() - 1) : null);
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(following);
        setLoading(false);
    }

    @Override
    public void GetFollowingFail(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }

    @Override
    public void GetFollowingException(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }
}
