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

    private class GetFollowingHandler extends Handler {
        private FollowingObserver observer;

        public GetFollowingHandler(FollowingObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.GetFollowingSuccess(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.GetFollowingFail("Failed to get following: " + message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.GetFollowingException("Failed to get following because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetFollowersHandler extends Handler {
        private FollowersObserver observer;

        public GetFollowersHandler(FollowersObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

                observer.GetFollowersSuccess(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.GetFollowersFail("Failed to get followers: " + message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.GetFollowersException("Failed to get followers because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetUserFollowerHandler extends Handler {
        private FollowersObserver observer;

        public GetUserFollowerHandler(FollowersObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.FollowerHolderSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.FollowerHolderFail("Failed to get user's profile: " + message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.FollowerHolderException("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetUserFollowingHandler extends Handler {
        private FollowingObserver observer;

        public GetUserFollowingHandler(FollowingObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.FollowingHolderSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.FollowingHolderFail("Failed to get user's profile: " + message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.FollowingHolderException("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }

    private class IsFollowerHandler extends Handler {

        private FollowMainObserver observer;

        public IsFollowerHandler(FollowMainObserver observer){
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                observer.isFollowerSuccess(msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY));
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.isFollowerFail("Failed to determine following relationship: " + message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.isFollowerException("Failed to determine following relationship because of exception: " + ex.getMessage());
            }
        }
    }

    private class UnfollowHandler extends Handler {
        private FollowMainObserver observer;

        public UnfollowHandler(FollowMainObserver observer){
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.unfollowUserSuccess(true);
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.unfollowUserFail("Failed to unfollow: " + message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                observer.unfollowUserExceptionException("Failed to unfollow because of exception: " + ex.getMessage());
            }

        }
    }

    private class FollowHandler extends Handler {
        private FollowMainObserver observer;

        public FollowHandler(FollowMainObserver observer){
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.followUserSuccess(false);
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.followUserFail("Failed to unfollow: " + message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.followUserExceptionException("Failed to unfollow because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetFollowersCountHandler extends Handler {
        private FollowMainObserver observer;

        public GetFollowersCountHandler(FollowMainObserver observer){
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                observer.getFollowersCountSuccess(msg.getData().getInt(GetFollowersCountTask.COUNT_KEY));
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.getFollowersCountFail("Failed to get followers count: " + message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.getFollowersCountException("Failed to get followers count because of exception: " + ex.getMessage());
            }
        }
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handler {
        private FollowMainObserver observer;

        public GetFollowingCountHandler(FollowMainObserver observer){
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                observer.getFollowingCountSuccess(msg.getData().getInt(GetFollowingCountTask.COUNT_KEY));
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.getFollowingCountFail("Failed to get following count: " + message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.getFollowingCountException("Failed to get following count because of exception: " + ex.getMessage());
            }
        }
    }
}