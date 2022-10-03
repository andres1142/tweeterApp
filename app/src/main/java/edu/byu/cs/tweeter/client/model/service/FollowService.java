package edu.byu.cs.tweeter.client.model.service;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowInteractionObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends GeneralService {

    public void IsFollower(User selectedUser, FollowInteractionObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecuteTask(isFollowerTask);
    }

    public void unfollowUser(User selectedUser, FollowInteractionObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        ExecuteTask(unfollowTask);
    }

    public void followUser(User selectedUser, FollowInteractionObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        ExecuteTask(followTask);
    }
    public void updateSelectedUserFollows(User selectedUser, FollowInteractionObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        ExecuteTask(followersCountTask);

        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        ExecuteTask(followingCountTask);
    }
}