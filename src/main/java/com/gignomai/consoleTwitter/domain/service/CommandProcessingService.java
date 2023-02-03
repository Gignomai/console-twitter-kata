package com.gignomai.consoleTwitter.domain.service;

import com.gignomai.consoleTwitter.domain.model.CommandEnum;
import com.gignomai.consoleTwitter.domain.model.exception.CommandNotFoundException;

import java.util.Arrays;

public class CommandProcessingService {

    public void processCommand(String command) throws CommandNotFoundException {
        Arrays.stream(CommandEnum.values())
                .filter(commandEnum -> commandEnum.checkCommandPattern(commandEnum.getPattern(), command))
                .map(CommandEnum::getCommand)
                .findFirst()
                .orElseThrow(() -> new CommandNotFoundException("Error"))
                .execute(command);
    }
}
