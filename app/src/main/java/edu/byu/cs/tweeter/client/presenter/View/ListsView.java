package edu.byu.cs.tweeter.client.presenter.View;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface ListsView extends View {
    void setLoading(boolean value);

    <T> void addItems(List<T> items);

    void updateInfoView(User user);
}
