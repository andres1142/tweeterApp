package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements StatusService.FeedObserver {

    private static final int PAGE_SIZE = 10;

    private FeedView view;

    private User user;
    private Status lastFeed;

    private boolean hasMorePages = true;
    private boolean isLoading = false;


    public interface FeedView {
        void setLoading(boolean value);

        void addItems(List<Status> statuses);

        void updateInfoView(User user);

        void displayInfoMessage(String message);
    }

    public FeedPresenter(User user, FeedView view){
        this.view = view;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getLastFeed() {
        return lastFeed;
    }

    public void setLastFeed(Status lastFeed) {
        this.lastFeed = lastFeed;
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

    public void LoadMoreItems(){
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            new StatusService().FeedLoadMore(user, PAGE_SIZE, lastFeed, this);
        }
    }

    public void FeedHolder(String username){
        view.displayInfoMessage("Getting user's profile...");
        new StatusService().FeedHolder(username, this);
    }

    @Override
    public void FeedHolderSuccess(User user) {
        view.updateInfoView(user);
    }

    @Override
    public void FeedHolderFail(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void FeedHolderException(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void GetFeedSuccess(List<Status> stories, boolean hasMorePages) {
        setLastFeed((stories.size() > 0) ? stories.get(stories.size() - 1) : null);
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems(stories);
        setLoading(false);
    }

    @Override
    public void GetFeedFail(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }

    @Override
    public void GetFeedException(String message) {
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }
}
