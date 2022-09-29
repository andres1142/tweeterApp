package edu.byu.cs.tweeter.client.backgroundTask.observer;

import java.util.List;

public interface HolderAdapterObserver extends ServiceObserver {
    <T> void handleHolderSuccess(T item);
    <T> void handleGetInfoSuccess(List<T> items, boolean hasMorePages);
    void handleGetInfoFailException(String message);
}
