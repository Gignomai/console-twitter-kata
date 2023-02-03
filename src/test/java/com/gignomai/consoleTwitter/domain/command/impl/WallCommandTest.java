package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.FollowPersistenceService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WallCommandTest {

    @Mock
    TweetPersistenceService tweetPersistenceService;
    @Mock
    FollowPersistenceService followPersistenceService;
    @Mock
    TweetOutputService tweetOutputService;

    WallCommand wallCommand;

    @BeforeEach
    private void setUp() {
        wallCommand = new WallCommand(tweetPersistenceService, followPersistenceService, tweetOutputService);
    }

    @Test
    void shouldReturnTweetsFromUserWall() {
        String command = "userName wall";
        String followerName = "userName";
        String followedName = "followedUser";

        when(followPersistenceService.getFollowedUsersNames(followerName)).thenReturn(List.of(followedName));

        Tweet followerTweet = Tweet.builder()
                .username(followerName)
                .message("message")
                .creationDate(Instant.now())
                .build();
        when(tweetPersistenceService.findTweetsByUserName(followerName)).thenReturn(List.of(followerTweet));

        Tweet followedTweet = Tweet.builder()
                .username(followedName)
                .message("message")
                .creationDate(Instant.now())
                .build();
        when(tweetPersistenceService.findTweetsByUserName(followedName)).thenReturn(List.of(followedTweet));

        wallCommand.execute(command);

        ArgumentCaptor<String> followerUserArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(followPersistenceService, times(1)).getFollowedUsersNames(followerUserArgumentCaptor.capture());
        String capturedFollowerUser = followerUserArgumentCaptor.getValue();
        assertNotNull(capturedFollowerUser);
        assertEquals(followerName, capturedFollowerUser);

        ArgumentCaptor<String> userArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(tweetPersistenceService, times(2)).findTweetsByUserName(userArgumentCaptor.capture());
        List<String> capturedUsers = userArgumentCaptor.getAllValues();
        assertFalse(capturedUsers.isEmpty());
        assertTrue(capturedUsers.contains(followedName));
        assertTrue(capturedUsers.contains(followerName));

        ArgumentCaptor<Tweet> tweetArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);
        verify(tweetOutputService, times(2)).sendWallMessageToOutput(tweetArgumentCaptor.capture());
        List<Tweet> capturedTweets = tweetArgumentCaptor.getAllValues();
        assertFalse(capturedTweets.isEmpty());
        assertTrue(capturedTweets.contains(followerTweet));
        assertTrue(capturedTweets.contains(followedTweet));
    }

    @Test
    void shouldNotSendToOutputIfThereAreNoMessages() {
        String command = "userName wall";

        when(tweetPersistenceService.findTweetsByUserName(anyString())).thenReturn(new ArrayList<>());
        when(followPersistenceService.getFollowedUsersNames(anyString())).thenReturn(new ArrayList<>());

        wallCommand.execute(command);

        ArgumentCaptor<String> followerUserArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(followPersistenceService, times(1)).getFollowedUsersNames(followerUserArgumentCaptor.capture());
        String capturedFollowerUser = followerUserArgumentCaptor.getValue();
        assertNotNull(capturedFollowerUser);
        assertEquals("userName", capturedFollowerUser);

        ArgumentCaptor<String> userArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(tweetPersistenceService, times(1)).findTweetsByUserName(userArgumentCaptor.capture());
        String capturedUser = userArgumentCaptor.getValue();
        assertNotNull(capturedUser);
        assertEquals("userName", capturedUser);

        verify(tweetOutputService, times(0)).sendWallMessageToOutput(any());
    }

    @Test
    void shouldNotReadWallIfNoUserInformed() {
        String command = "wall";

        wallCommand.execute(command);

        verify(followPersistenceService, times(0)).getFollowedUsersNames(any());
        verify(tweetPersistenceService, times(0)).findTweetsByUserName(any());
        verify(tweetOutputService, times(0)).sendWallMessageToOutput(any());
    }
}