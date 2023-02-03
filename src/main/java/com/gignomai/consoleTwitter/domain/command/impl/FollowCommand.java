package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.command.ICommand;
import com.gignomai.consoleTwitter.domain.model.Follow;
import com.gignomai.consoleTwitter.domain.port.secondary.FollowPersistenceService;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gignomai.consoleTwitter.domain.model.CommandEnum.FOLLOW;

public class FollowCommand implements ICommand {

    private final FollowPersistenceService followPersistenceService;

    @Inject
    public FollowCommand(FollowPersistenceService followPersistenceService) {
        this.followPersistenceService = followPersistenceService;
    }

    @Override
    public void execute(String command) {
        Pattern pattern = Pattern.compile(FOLLOW.getPattern());
        Matcher matcher = pattern.matcher(command.trim());

        if (isValidCommand(matcher)) {
            followPersistenceService.saveFollow(Follow.builder()
                    .followerUser(matcher.group(1))
                    .followedUser(matcher.group(2))
                    .build());
        }
    }

    private boolean isValidCommand(Matcher matcher) {
        if (!matcher.find()) {
            return false;
        } else {
            String follower = matcher.group(1);
            String followed = matcher.group(2);

            return !follower.equals(followed);
        }
    }
}
