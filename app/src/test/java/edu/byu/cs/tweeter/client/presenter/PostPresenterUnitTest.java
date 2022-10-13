package edu.byu.cs.tweeter.client.presenter;

import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.ParseException;

import edu.byu.cs.tweeter.client.backgroundTask.observer.UserInteractionObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.View.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostPresenterUnitTest {
    private MainView mockView;
    private Cache mockCache;
    private StatusService mockStatusService;
    private User user;
    private Status testStatus;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setUp() throws ParseException {
        user = new User("Andres", "Molina", "anotherClone", "http/sadfasf");
        String post = "Test Post";

        //Creating mocks for our classes
        mockView = Mockito.mock(MainView.class);
        mockCache = Mockito.mock(Cache.class);
        mockStatusService = Mockito.mock(StatusService.class);

        Cache.setInstance(mockCache);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getService()).thenReturn(mockStatusService);

        testStatus = new Status(post, user, mainPresenterSpy.getFormattedDateTime(),
                mainPresenterSpy.parseURLs(post), mainPresenterSpy.parseMentions(post));
    }

    @Test
    public void postStatusSuccess() {
        Answer<Void> answer = invocation -> {

            UserInteractionObserver observer = invocation.getArgument(1, UserInteractionObserver.class);
            observer.handlePostSuccess();

            return null;
        };
        TestPostCall("Successfully Posted!", answer);
    }

    @Test
    public void postStatusFail() {
        Answer<Void> answer = invocation -> {

            UserInteractionObserver observer = invocation.getArgument(1, UserInteractionObserver.class);;
            observer.handlePostFail("Error");

            return null;
        };
        TestPostCall("Failed to post status: " + "Error", answer);
    }

    @Test
    public void postStatusException(){
        Answer<Void> answer = invocation -> {

            UserInteractionObserver observer = invocation.getArgument(1, UserInteractionObserver.class);
            observer.handlePostException("Exception");

            return null;
        };
        TestPostCall("Failed to post status because of exception: " + "Exception" , answer);

    }

    public void TestPostCall(String message, Answer answer){
        Mockito.doAnswer(answer).when(mockStatusService).PostStatus(Mockito.any(Status.class), Mockito.any(UserInteractionObserver.class));
        mainPresenterSpy.postStatus(testStatus);

        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayInfoMessage(message);
    }
}
