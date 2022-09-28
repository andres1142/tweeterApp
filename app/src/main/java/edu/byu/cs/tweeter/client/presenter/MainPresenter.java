package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteraction;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements FollowService.FollowMainObserver, UserService.LogOutObserver,
        StatusService.PostStatusObserver, FollowInteraction {

    MainView view;


    public interface MainView {
        void isFollower(boolean isFollower);

        void updateFollowingFollowers(boolean value);

        void setFollowerCount(int value);

        void setFollowingCount(int value);

        void enableFollowButton(boolean value);

        void displayInfoMessage(String message);

        void clearInfoMessage();

        void logOutUser();
    }

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void isFollower(User selectedUser) {
        new FollowService().IsFollower(selectedUser, this);
    }

    public void postStatus(Status newStatus) {
        view.clearInfoMessage();
        new StatusService().PostStatus(newStatus, this);
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

    @Override
    public void isFollowerSuccess(boolean isFollower) {
        view.isFollower(isFollower);
    }

    @Override
    public void isFollowerFail(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void isFollowerException(String message) {
        view.displayInfoMessage(message);
    }


    @Override
    public void unfollowUserSuccess(boolean value) {
        view.updateFollowingFollowers(value);
        view.enableFollowButton(true);
    }

    @Override
    public void followUserSuccess(boolean value) {
        view.updateFollowingFollowers(value);
        view.enableFollowButton(true);
    }

    @Override
    public void getFollowersCountSuccess(int value) {
        view.setFollowerCount(value);
    }


    @Override
    public void getFollowingCountSuccess(int value) {
        view.setFollowingCount(value);
    }



    @Override
    public void ExceptionFailFollowUnfollow(String message){
        view.displayInfoMessage(message);
        view.enableFollowButton(true);
    }


    @Override
    public void handleExceptionAndFail(String message) {
        view.displayInfoMessage(message);
    }



    //User Service
    @Override
    public void logOutSuccess() {
        view.logOutUser();
    }

    @Override
    public void logOutFail(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void logOutException(String message) {
        view.displayInfoMessage(message);
    }

    //Status Service
    @Override
    public void postSuccess() {
        view.displayInfoMessage("Successfully Posted!");
    }

    @Override
    public void postFail(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void postException(String message) {
        view.displayInfoMessage(message);
    }
}
