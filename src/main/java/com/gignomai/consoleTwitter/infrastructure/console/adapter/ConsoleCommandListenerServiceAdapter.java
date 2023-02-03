package com.gignomai.consoleTwitter.infrastructure.console.adapter;

import com.gignomai.consoleTwitter.domain.model.exception.CommandNotFoundException;
import com.gignomai.consoleTwitter.domain.port.primary.CommandListenerService;
import com.gignomai.consoleTwitter.domain.service.CommandProcessingService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.Scanner;

@Slf4j
public class ConsoleCommandListenerServiceAdapter implements CommandListenerService {

    public static final String QUIT_COMMAND = "quit";
    private final CommandProcessingService commandProcessingService;

    @Inject
    public ConsoleCommandListenerServiceAdapter(CommandProcessingService commandProcessingService) {
        this.commandProcessingService = commandProcessingService;
    }

    @Override
    public void listen(InputStream inputStream) {
        Scanner in = new Scanner(inputStream);

        boolean quitCommandReceived = false;
        while (!quitCommandReceived) {
            System.out.print("> ");
            String command = in.nextLine();

            if (!command.isEmpty()) {
                if(command.equalsIgnoreCase(QUIT_COMMAND)) {
                    quitCommandReceived = true;
                } else {
                    try {
                        commandProcessingService.processCommand(command);
                    } catch (CommandNotFoundException e) {
                        System.out.println("Command " + command + " not found. Please try another command.");
                    }
                }
            }
        }
    }
}
