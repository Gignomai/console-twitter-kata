package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.command.ICommand;
import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetOutputService;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetPersistenceService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gignomai.consoleTwitter.domain.model.CommandEnum.READ;

@Slf4j
public class ReadCommand implements ICommand {

    private final TweetPersistenceService tweetPersistenceService;
    private final TweetOutputService tweetOutputService;

    @Inject
    public ReadCommand(TweetPersistenceService tweetPersistenceService, TweetOutputService tweetOutputService) {
        this.tweetPersistenceService = tweetPersistenceService;
        this.tweetOutputService = tweetOutputService;
    }

    @Override
    public void execute(String command) {
        Pattern pattern = Pattern.compile(READ.getPattern());
        Matcher matcher = pattern.matcher(command.trim());

        if(matcher.find()) {
            String user = matcher.group(1);
            ArrayList<Tweet> tweets = new ArrayList<>(tweetPersistenceService.findTweetsByUserName(user));
            if(tweets.isEmpty()) {
                log.debug("No tweets found for user: {}", user);
            } else {
                tweets.sort(Comparator.comparing(Tweet::getCreationDate).reversed());
                tweets.forEach(tweetOutputService::sendReadMessageToOutput);
            }
        }
    }
}
