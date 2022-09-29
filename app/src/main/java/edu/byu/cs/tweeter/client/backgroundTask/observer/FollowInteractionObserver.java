package edu.byu.cs.tweeter.client.backgroundTask.observer;

public interface FollowInteractionObserver extends ServiceObserver {
    void handleSuccessFollowUnfollow(boolean value);
    void ExceptionFailFollowUnfollow(String message);
    void handleFollowerCount(int value);
    void handleFollowingCount(int value);
    void handleIsFollower(boolean value);
}
