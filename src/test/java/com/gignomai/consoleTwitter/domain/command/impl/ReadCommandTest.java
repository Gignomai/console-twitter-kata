package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetOutputService;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadCommandTest {

    @Mock
    TweetPersistenceService tweetPersistenceService;
    @Mock
    TweetOutputService tweetOutputService;

    private ReadCommand readCommand;

    @BeforeEach
    private void setUp(){
        readCommand = new ReadCommand(tweetPersistenceService, tweetOutputService);
    }

    @Test
    void shouldReadUserPostsIfExists() {
        shouldReadUserPosts("username");
    }

    @Test
    void shouldReadUserPostsWithSpaces() {
        shouldReadUserPosts(" username ");
    }

    void shouldReadUserPosts(String command) {
        Tweet tweet = Tweet.builder()
                .username(command)
                .message("message")
                .creationDate(Instant.now())
                .build();
        when(tweetPersistenceService.findTweetsByUserName(anyString())).thenReturn(List.of(tweet));

        readCommand.execute(command);

        ArgumentCaptor<String> userArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(tweetPersistenceService, times(1)).findTweetsByUserName(userArgumentCaptor.capture());
        String userName = userArgumentCaptor.getValue();
        assertNotNull(userName);
        assertEquals(command.trim(), userName);

        ArgumentCaptor<Tweet> messageArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);
        verify(tweetOutputService, times(1)).sendReadMessageToOutput(messageArgumentCaptor.capture());
        Tweet capturedTweet = messageArgumentCaptor.getValue();
        assertNotNull(capturedTweet);
        assertEquals(tweet, capturedTweet);
    }

    @Test
    void shouldNotReadUserPostsIfUserDoesNotExistOrHasNoTweets() {
        String command = "fakeUser";

        when(tweetPersistenceService.findTweetsByUserName(command)).thenReturn(new ArrayList<>());

        readCommand.execute(command);

        ArgumentCaptor<String> userArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(tweetPersistenceService, times(1)).findTweetsByUserName(userArgumentCaptor.capture());
        String userName = userArgumentCaptor.getValue();
        assertNotNull(userName);
        assertEquals(command, userName);

        verify(tweetOutputService, times(0)).sendReadMessageToOutput(any(Tweet.class));
    }

    @Test
    void shouldNotReadUserPostsIfUserNotInformed() {
        String command = " ";

        readCommand.execute(command);

        verify(tweetPersistenceService, times(0)).findTweetsByUserName(anyString());
        verify(tweetOutputService, times(0)).sendReadMessageToOutput(any(Tweet.class));
    }

}