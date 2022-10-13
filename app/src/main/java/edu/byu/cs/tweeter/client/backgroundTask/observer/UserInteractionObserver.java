package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface UserInteractionObserver extends ServiceObserver{
    void handleUserInteractionSuccess(User user, AuthToken authToken);
    void handlePostSuccess();
    void handlePostFail(String message);
    void handlePostException(String message);
}
