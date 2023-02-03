package com.gignomai.consoleTwitter.infrastructure.console.adapter;

import com.gignomai.consoleTwitter.domain.model.exception.CommandNotFoundException;
import com.gignomai.consoleTwitter.domain.port.primary.CommandListenerService;
import com.gignomai.consoleTwitter.domain.service.CommandProcessingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsoleCommandListenerServiceAdapterTest {

    @Mock
    CommandProcessingService commandProcessingService;

    CommandListenerService commandListenerService;

    @BeforeEach
    public void setUp() {
        commandListenerService = new ConsoleCommandListenerServiceAdapter(commandProcessingService);
    }

    @Test
    void shouldStartListeningCommands() throws CommandNotFoundException {
        commandListenerService.listen(getClass().getClassLoader().getResourceAsStream("commands.dat"));

        ArgumentCaptor<String> commandArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(commandProcessingService, times(6)).processCommand(commandArgumentCaptor.capture());
        assertTrue(commandArgumentCaptor.getAllValues().containsAll(List.of("user -> message",
                "user",
                "user wall",
                "user2 -> message2",
                "user follows user2",
                "user wall")));
    }


    @Test
    void shouldNotThrowExceptionWhenCommandDoesNotExist() throws CommandNotFoundException {
        doThrow(new CommandNotFoundException("Command not found!!!"))
                .when(commandProcessingService).processCommand(anyString());
        Assertions.assertDoesNotThrow(() -> commandListenerService
                .listen(getClass().getClassLoader().getResourceAsStream("commands.dat")));
    }
}