package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserFollowingHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface FollowMainObserver {
        void isFollowerSuccess(boolean isFollower);
        void isFollowerFail(String message);
        void isFollowerException(String message);

        void unfollowUserSuccess(boolean value);
        void unfollowUserFail(String message);
        void unfollowUserExceptionException(String message);

        void followUserSuccess(boolean value);
        void followUserFail(String message);
        void followUserExceptionException(String message);

        void getFollowersCountSuccess(int value);
        void getFollowersCountFail(String message);
        void getFollowersCountException(String message);

        void getFollowingCountSuccess(int value);
        void getFollowingCountFail(String message);
        void getFollowingCountException(String message);
    }

    public interface FollowingObserver {

        void FollowingHolderSuccess(User user);

        void FollowingHolderFail(String message);

        void FollowingHolderException(String message);

        void GetFollowingSuccess(List<User> following, boolean hasMorePages);

        void GetFollowingFail(String message);

        void GetFollowingException(String message);
    }

    public interface FollowersObserver {

        void FollowerHolderSuccess(User user);

        void FollowerHolderFail(String message);

        void FollowerHolderException(String message);

        void GetFollowersSuccess(List<User> followers, boolean hasMorePages);

        void GetFollowersFail(String message);

        void GetFollowersException(String message);
    }

    public void IsFollower(User selectedUser, FollowMainObserver observer){
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void unfollowUser(User selectedUser, FollowMainObserver observer){
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }
    public void followUser(User selectedUser, FollowMainObserver observer){
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }


    public void FollowersHolder(String username, FollowersObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new GetUserFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void FollowingHolder(String username, FollowingObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new GetUserFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void FollowersLoadMore(User user, int PAGE_SIZE, User lastFollower, FollowersObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void FollowingLoadMore(User user, int PAGE_SIZE, User lastFollowee, FollowingObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void updateSelectedUserFollows(User selectedUser, FollowMainObserver observer){
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }
}