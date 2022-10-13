package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.presenter.View.ListsView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class FollowInteractionPresenter implements HolderAdapterObserver {

    public static final int PAGE_SIZE = 10;

    public ListsView view;

    public User user;
    public User lastFollow;
    public AuthToken authToken;

    public boolean hasMorePages = true;
    public boolean isLoading = false;


    public void setLastFollow(User lastFollower) {
        this.lastFollow = lastFollower;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    FollowInteractionPresenter(ListsView view, User user, AuthToken token) {
        this.authToken = token;
        this.view = view;
        this.user = user;
    }
    FollowInteractionPresenter(ListsView view, User user) {
        this.view = view;
        this.user = user;
    }
}
