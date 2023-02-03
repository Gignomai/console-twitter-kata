package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostCommandTest {

    @Mock
    TweetPersistenceService tweetPersistenceService;

    private PostCommand postCommand;

    @BeforeEach
    private void setUp() {
        postCommand = new PostCommand(tweetPersistenceService);
    }

    @Test
    void shouldPostTweet() {
        String command = "userName -> The Message";
        Instant instant = Instant.now();

        postCommand.execute(command);

        ArgumentCaptor<Tweet> postArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);
        verify(tweetPersistenceService, times(1)).saveTweet(postArgumentCaptor.capture());
        Tweet post = postArgumentCaptor.getValue();
        assertNotNull(post);
        assertEquals("userName", post.getUsername());
        assertEquals("The Message", post.getMessage());
        assertTrue(instant.isBefore(post.getCreationDate()));
    }

    @Test
    void shouldNotPostTweetIfUserDoesNotExist() {
        String command = " -> message";
        testErrorPostCommand(command);
    }

    @Test
    void shouldNotPostTweetIfMessageIsEmpty() {
        String command = "user -> ";
        testErrorPostCommand(command);
    }

    @Test
    void shouldNotPostTweetIUserDoesNotExistAndMessageIsEmpty() {
        String command = " -> ";
        testErrorPostCommand(command);
    }

    private void testErrorPostCommand(String command) {
        postCommand.execute(command);

        verify(tweetPersistenceService, times(0)).saveTweet(any(Tweet.class));
    }
}