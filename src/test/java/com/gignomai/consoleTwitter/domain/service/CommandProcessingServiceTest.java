package com.gignomai.consoleTwitter.domain.service;

import com.gignomai.consoleTwitter.domain.model.exception.CommandNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandProcessingServiceTest {

    private CommandProcessingService commandProcessingService;

    @BeforeEach
    private void setUp() {
        commandProcessingService = new CommandProcessingService();
    }

    @Test
    void shouldProcessPostCommand() {
        String command = "user -> message";
        Assertions.assertDoesNotThrow(() -> commandProcessingService.processCommand(command));
    }

    @Test
    void shouldProcessReadCommand() {
        String command = "user";
        Assertions.assertDoesNotThrow(() -> commandProcessingService.processCommand(command));
    }

    @Test
    void shouldProcessFollowCommand() {
        String command = "user1 follows user2";
        Assertions.assertDoesNotThrow(() -> commandProcessingService.processCommand(command));
    }

    @Test
    void shouldProcessWallCommand() {
        String command = "user wall";
        Assertions.assertDoesNotThrow(() -> commandProcessingService.processCommand(command));
    }

    @Test
    void shouldNotProcessCommand() {
        String command = "error command";
        Assertions.assertThrows(CommandNotFoundException.class, () -> commandProcessingService.processCommand(command));
    }
}