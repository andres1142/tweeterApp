package edu.byu.cs.tweeter.client.backgroundTask.observer;

public interface FollowInteraction extends ServiceObserver {
    void handleSuccessFollowUnfollow(boolean value);
    void ExceptionFailFollowUnfollow(String message);
}
