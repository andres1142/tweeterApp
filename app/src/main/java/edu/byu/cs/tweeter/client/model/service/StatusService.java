package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public interface StoryObserver {
        void StoryHolderSuccess(User user);

        void StoryHolderFail(String message);

        void StoryHolderException(String message);


        void GetStorySuccess(List<Status> stories, boolean hasMorePages);

        void GetStoryFail(String message);

        void GetStoryException(String message);
    }

    public interface FeedObserver {

        void FeedHolderSuccess(User user);

        void FeedHolderFail(String message);

        void FeedHolderException(String message);

        void GetFeedSuccess(List<Status> stories, boolean hasMorePages);

        void GetFeedFail(String message);

        void GetFeedException(String message);
    }

    public interface PostStatusObserver{
        void postSuccess();
        void postFail(String message);
        void postException(String message);
    }

    public void StoryLoadMore(User user, int PAGE_SIZE, Status lastStatus, StoryObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetStoryHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    public void StoryHolder(String username, StoryObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void FeedLoadMore(User user, int PAGE_SIZE, Status lastStatus, FeedObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    public void FeedHolder(String username, FeedObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new GetUserFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void PostStatus(Status newStatus, PostStatusObserver observer){
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }

    private class GetUserHandler extends Handler {
        private StoryObserver observer;

        public GetUserHandler(StoryObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.StoryHolderSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.StoryHolderFail("Failed to get user's profile: " + message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.StoryHolderException("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }


    private class GetStoryHandler extends Handler {

        private StoryObserver observer;

        public GetStoryHandler(StoryObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> stories = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
                observer.GetStorySuccess(stories, hasMorePages);
            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.GetStoryFail("Failed to get story: " + message);
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.GetStoryException("Failed to get story because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetUserFeedHandler extends Handler {
        private FeedObserver observer;

        public GetUserFeedHandler(FeedObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.FeedHolderSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.FeedHolderFail("Failed to get user's profile: " + message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.FeedHolderException("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetFeedHandler extends Handler {
        private FeedObserver observer;

        public GetFeedHandler(FeedObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> stories = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
                observer.GetFeedSuccess(stories, hasMorePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.GetFeedFail("Failed to get feed: " + message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.GetFeedException("Failed to get feed because of exception: " + ex.getMessage());
            }
        }
    }



    private class PostStatusHandler extends Handler {
        private PostStatusObserver observer;

        public PostStatusHandler(PostStatusObserver observer) {
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
            if (success) {
                observer.postSuccess();
            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
                observer.postFail("Failed to post status: " + message);
            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
                observer.postException("Failed to post status because of exception: " + ex.getMessage());
            }
        }
    }
}
