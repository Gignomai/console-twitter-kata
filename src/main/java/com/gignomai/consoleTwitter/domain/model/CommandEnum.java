package com.gignomai.consoleTwitter.domain.model;

import com.gignomai.consoleTwitter.domain.command.ICommand;
import com.gignomai.consoleTwitter.domain.command.impl.FollowCommand;
import com.gignomai.consoleTwitter.domain.command.impl.PostCommand;
import com.gignomai.consoleTwitter.domain.command.impl.ReadCommand;
import com.gignomai.consoleTwitter.domain.command.impl.WallCommand;
import com.gignomai.consoleTwitter.infrastructure.console.adapter.ConsoleTweetOutputServiceAdapter;
import com.gignomai.consoleTwitter.infrastructure.repository.adapter.SQLFollowPersistenceServiceAdapter;
import com.gignomai.consoleTwitter.infrastructure.repository.adapter.SQLTweetPersistenceServiceAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public enum CommandEnum {
    POST("^(\\w+)\\s?->\\s?(.+)", new PostCommand(new SQLTweetPersistenceServiceAdapter())),
    READ("^(\\w+)$", new ReadCommand(new SQLTweetPersistenceServiceAdapter(),
            new ConsoleTweetOutputServiceAdapter())),
    WALL("^(\\w+)\\swall$", new WallCommand(new SQLTweetPersistenceServiceAdapter(),
            new SQLFollowPersistenceServiceAdapter(), new ConsoleTweetOutputServiceAdapter())),
    FOLLOW("^(\\w+)\\sfollows\\s(\\w+)$", new FollowCommand(new SQLFollowPersistenceServiceAdapter()));

    private final String pattern;
    private final ICommand command;

    public boolean checkCommandPattern(String commandPattern, String command) {
        Pattern pattern = Pattern.compile(commandPattern);
        Matcher matcher = pattern.matcher(command);

        return matcher.find();
    }
}
