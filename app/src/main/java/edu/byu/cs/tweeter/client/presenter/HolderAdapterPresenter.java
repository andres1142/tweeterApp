package edu.byu.cs.tweeter.client.presenter;


import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.View.ListsView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class HolderAdapterPresenter implements HolderAdapterObserver {

    private static final int PAGE_SIZE = 10;

    private ListsView view;

    private AuthToken authToken;
    private User user;
    private Status lastStatus;
    private User lastUser;

    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public User getUser() {
        return user;
    }

    protected void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    public Status getLastStatus() {
        return lastStatus;
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

    public int getPAGE_SIZE() {
        return PAGE_SIZE;
    }

    public User getLastUser() {
        return lastUser;
    }

    public void setLastUser(User lastUser) {
        this.lastUser = lastUser;
    }

    public ListsView getView() {
        return view;
    }

    public HolderAdapterPresenter(User user, ListsView view) {
        this.view = view;
        this.user = user;
    }

    public HolderAdapterPresenter(User user, AuthToken token, ListsView view) {
        this.view = view;
        this.user = user;
        this.authToken = token;
    }

    public void getHolder(String username){
        getView().displayInfoMessage("Getting user's profile...");
        new StatusService().GetUser(username, this);
    }

    public void LoadMoraItems(boolean isUser){
        if (!isLoading() && isHasMorePages()) {
            setLoading(true);
            view.setLoading(true);

            if(isUser){
                new FollowService().GetUsersList(getUser(), getPAGE_SIZE(), getLastUser(), this);
            } else {
                new StatusService().GetStatusList(getUser(), getPAGE_SIZE(), getLastStatus(), this);
            }
        }
    }



    @Override
    public <T> void handleHolderSuccess(T item) {
        getView().updateInfoView((User) item);
    }

    @Override
    public <T> void handleGetInfoSuccess(List<T> items, boolean hasMorePages) {
        boolean isUser = false;
        if(items.get(items.size() - 1).getClass() == user.getClass()){
            setLastUser((items.size() > 0) ? (User) items.get(items.size() - 1) : null);
            isUser = true;
        } else {
            setLastStatus((items.size() > 0) ? (Status) items.get(items.size() - 1) : null);
        }
        setHasMorePages(hasMorePages);
        getView().setLoading(false);
        if(isUser) {
            getView().addItems((List<User>) items);
        } else {
            getView().addItems((List<Status>) items);
        }
        setLoading(false);
    }

    @Override
    public void handleGetInfoFailException(String message) {
        getView().setLoading(false);
        getView().displayInfoMessage(message);
        setLoading(false);
    }

    @Override
    public void handleExceptionAndFail(String message) {
        getView().displayInfoMessage(message);
    }
}
