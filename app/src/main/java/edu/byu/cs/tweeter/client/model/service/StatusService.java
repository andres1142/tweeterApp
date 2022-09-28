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
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserFeedHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.PostStatusHandler;
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
}
