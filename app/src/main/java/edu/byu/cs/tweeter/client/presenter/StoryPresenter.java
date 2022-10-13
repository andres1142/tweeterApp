package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.View.ListsView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends StatusInteractionPresenter {


    public StoryPresenter(User user, ListsView view) {
        super(user, view);
    }


    public void LoadMoreItems() {
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            new StatusService().GetStatusList(user, PAGE_SIZE, lastStatus, this);
        }
    }

    public void StoryHolder(String username) {
        view.displayInfoMessage("Getting user's profile...");
        new StatusService().GetUser(username, this);
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