package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.View.ListsView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends FollowInteractionPresenter {

    public FollowersPresenter(User user, ListsView view){
        super(view, user);
    }

    public void FollowersHolder(String username){
        view.displayInfoMessage("Getting user's profile...");
        new FollowService().GetUser(username, this);
    }

    public void LoadMoreItems(){
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            new FollowService().GetUsersList(user, PAGE_SIZE, lastFollow, this);
        }
    }

    @Override
    public <T> void handleHolderSuccess(T item) {
        view.updateInfoView((User) item);
    }

    @Override
    public <T> void handleGetInfoSuccess(List<T> items, boolean hasMorePages) {
        setLastFollow((items.size() > 0) ? (User) items.get(items.size() - 1) : null);
        setHasMorePages(hasMorePages);

        view.setLoading(false);
        view.addItems((List<User>) items);
        setLoading(false);
    }

    @Override
    public void handleGetInfoFailException(String message){
        view.setLoading(false);
        view.displayInfoMessage(message);
        setLoading(false);
    }

    @Override
    public void handleExceptionAndFail(String message) {
        view.displayInfoMessage(message);
    }
}