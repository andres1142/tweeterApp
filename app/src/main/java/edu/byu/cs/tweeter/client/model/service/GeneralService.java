package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetListHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.HolderAdapterObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GeneralService {
    public void GetUser(String username, HolderAdapterObserver observer) {
        GetUserTask GetUser = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new GetUserHandler(observer));
        ExecuteTask(GetUser);
    }
    public void GetUsersList(User user, int PAGE_SIZE, User lastFollower, HolderAdapterObserver observer){
        GetFollowersTask GetUsersList = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollower, new GetListHandler(observer));
        ExecuteTask(GetUsersList);
    }
    public void GetStatusList(User user, int PAGE_SIZE, Status lastStatus, HolderAdapterObserver observer) {
        GetFeedTask GetStatusList = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetListHandler(observer));
        ExecuteTask(GetStatusList);
    }

    public void ExecuteTask(BackgroundTask task){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
