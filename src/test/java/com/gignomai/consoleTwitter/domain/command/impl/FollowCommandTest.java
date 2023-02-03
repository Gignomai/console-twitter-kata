package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.model.Follow;
import com.gignomai.consoleTwitter.domain.port.secondary.FollowPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FollowCommandTest {

    @Mock
    FollowPersistenceService followPersistenceService;

    FollowCommand followCommand;

    @BeforeEach
    private void setUp() {
        followCommand = new FollowCommand(followPersistenceService);
    }

    @Test
    void shouldFollowUser() {
        String command = "user1 follows user2";
        Follow follow = Follow.builder()
                .followerUser("user1")
                .followedUser("user2")
                .build();

        followCommand.execute(command);

        ArgumentCaptor<Follow> followArgumentCaptor = ArgumentCaptor.forClass(Follow.class);
        verify(followPersistenceService, times(1)).saveFollow(followArgumentCaptor.capture());
        Follow capturedFollow = followArgumentCaptor.getValue();
        assertNotNull(capturedFollow);
        assertEquals(follow, capturedFollow);
    }

    @Test
    void shouldNotSaveFollowIfFollowerDoesNotExist() {
        String command = " follows user2";
        testErrorFollowCommand(command);
    }

    @Test
    void shouldNotSaveFollowIfFollowedDoesNotExist() {
        String command = "user follows";
        testErrorFollowCommand(command);
    }

    @Test
    void shouldNotSaveFollowIfNoneOfTheUsersExist() {
        String command = "follows";
        testErrorFollowCommand(command);
    }

    @Test
    void shouldNotSaveFollowIfUsersAreTheSame() {
        String command = "user follows user";
        testErrorFollowCommand(command);
    }

    private void testErrorFollowCommand(String command) {
        followCommand.execute(command);

        verify(followPersistenceService, times(0)).saveFollow(any(Follow.class));
    }
}