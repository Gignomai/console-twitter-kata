package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.command.ICommand;
import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetPersistenceService;

import javax.inject.Inject;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gignomai.consoleTwitter.domain.model.CommandEnum.POST;

public class PostCommand implements ICommand {

    private final TweetPersistenceService tweetPersistenceService;

    @Inject
    public PostCommand(TweetPersistenceService tweetPersistenceService) {
        this.tweetPersistenceService = tweetPersistenceService;
    }

    @Override
    public void execute(String command) {
        Pattern pattern = Pattern.compile(POST.getPattern());
        Matcher matcher = pattern.matcher(command.trim());

        if(matcher.find()) {
            tweetPersistenceService.saveTweet(Tweet.builder()
                    .username(matcher.group(1))
                    .message(matcher.group(2))
                    .creationDate(Instant.now())
                    .build());
        }
    }
}
