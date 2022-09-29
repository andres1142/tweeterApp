package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements HolderAdapterObserver {
    public static final int PAGE_SIZE = 10;

    private StoryView view;

    private User user;
    private Status lastStatus;

    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public interface StoryView {
        void setLoading(boolean value);

        void addItems(List<Status> statuses);

        void updateInfoView(User user);

        void displayInfoMessage(String message);
    }

    public StoryPresenter(User user, StoryView view) {
        this.user = user;
        this.view = view;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
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

    public void LoadMoreItems() {
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            new StatusService().StoryLoadMore(user, PAGE_SIZE, lastStatus, this);
        }
    }

    public void StoryHolder(String username) {
        view.displayInfoMessage("Getting user's profile...");
        new StatusService().StoryHolder(username, this);
    }


    @Override
    public <T> void handleHolderSuccess(T item) {
        view.updateInfoView(user);
    }

    @Override
    public <T> void handleGetInfoSuccess(List<T> items, boolean hasMorePages) {
        setLastStatus((items.size() > 0) ? (Status) items.get(items.size() - 1) : null);
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems((List<Status>) items);
        setLoading(false);
    }

    @Override
    public void handleGetInfoFailException(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }

    @Override
    public void handleExceptionAndFail(String message) {
        view.displayInfoMessage(message);
    }
}
