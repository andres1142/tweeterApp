package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.View.MainView;
import edu.byu.cs.tweeter.client.presenter.View.UserInteractionView;
import edu.byu.cs.tweeter.client.presenter.View.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserInteractionObserver, FollowInteractionObserver {

    MainView view;
    StatusService statusService;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    protected StatusService getService() {
        if(statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void isFollower(User selectedUser) {
        new FollowService().IsFollower(selectedUser, this);
    }

    public void postStatus(Status newStatus) {
        view.clearInfoMessage();
        getService().PostStatus(newStatus, this);
    }


    public void unfollowUser(User selectedUser) {
        view.clearInfoMessage();
        view.displayInfoMessage("Removing " + selectedUser.getName() + "...");
        new FollowService().unfollowUser(selectedUser, this);
    }

    public void followUser(User selectedUser) {
        view.clearInfoMessage();
        view.displayInfoMessage("Adding " + selectedUser.getName() + "...");
        new FollowService().followUser(selectedUser, this);
    }

    public void updateSelectedUserFollows(User selectedUser){
        new FollowService().updateSelectedUserFollows(selectedUser, this);
    }

    public void logOutUser() {
        view.clearInfoMessage();
        new UserService().LogOut(this);

    }


    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    //FollowInteraction
    @Override
    public void handleSuccessFollowUnfollow(boolean value) {
        view.updateFollowingFollowers(value);
        view.enableFollowButton(true);
    }

    @Override
    public void ExceptionFailFollowUnfollow(String message){
        view.displayInfoMessage(message);
        view.enableFollowButton(true);
    }

    @Override
    public void handleFollowerCount(int value) {
        view.setFollowerCount(value);
    }

    @Override
    public void handleFollowingCount(int value) {
        view.setFollowingCount(value);
    }

    @Override
    public void handleIsFollower(boolean value){
        view.isFollower(value);
    }

    @Override
    public void handleUserInteractionSuccess(User user, AuthToken authToken) {
        view.logOutUser();
    }

    @Override
    public void handlePostSuccess(){
        view.displayInfoMessage("Successfully Posted!");
    }

    @Override
    public void handlePostFail(String message) {
        view.displayInfoMessage("Failed to post status: " + message);
    }

    @Override
    public void handlePostException(String message) {
        view.displayInfoMessage("Failed to post status because of exception: " + message);
    }

    @Override
    public void handleExceptionAndFail(String message) {
        view.displayInfoMessage(message);
    }
}
