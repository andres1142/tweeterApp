package edu.byu.cs.tweeter.client.presenter.View;

public interface MainView extends UserInteractionView{
    void isFollower(boolean isFollower);

    void updateFollowingFollowers(boolean value);

    void setFollowerCount(int value);

    void setFollowingCount(int value);

    void enableFollowButton(boolean value);

    void logOutUser();
}
