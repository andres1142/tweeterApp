package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements HolderAdapterObserver {

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
    public <T> void handleHolderSuccess(T item) {
        view.updateInfoView((User) item);
    }

    @Override
    public <T> void handleGetInfoSuccess(List<T> items, boolean hasMorePages) {
        setLastFeed((items.size() > 0) ? (Status) items.get(items.size() - 1) : null);
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
