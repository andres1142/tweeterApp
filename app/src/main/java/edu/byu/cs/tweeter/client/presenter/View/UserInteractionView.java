package edu.byu.cs.tweeter.client.presenter.View;


public interface UserInteractionView extends View{
    void clearInfoMessage();

    void displayErrorMessage(String message);
}
