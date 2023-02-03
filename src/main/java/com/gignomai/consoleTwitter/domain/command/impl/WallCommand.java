package com.gignomai.consoleTwitter.domain.command.impl;

import com.gignomai.consoleTwitter.domain.command.ICommand;
import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.FollowPersistenceService;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetOutputService;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetPersistenceService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gignomai.consoleTwitter.domain.model.CommandEnum.WALL;

@Slf4j
public class WallCommand implements ICommand {

    private final TweetPersistenceService tweetPersistenceService;
    private final FollowPersistenceService followPersistenceService;
    private final TweetOutputService tweetOutputService;

    @Inject
    public WallCommand(TweetPersistenceService tweetPersistenceService, FollowPersistenceService followPersistenceService,
                       TweetOutputService tweetOutputService) {
        this.tweetPersistenceService = tweetPersistenceService;
        this.followPersistenceService = followPersistenceService;
        this.tweetOutputService = tweetOutputService;
    }

    @Override
    public void execute(String command) {
        Pattern pattern = Pattern.compile(WALL.getPattern());
        Matcher matcher = pattern.matcher(command.trim());

        if(matcher.find()) {
            String user = matcher.group(1);
            ArrayList<Tweet> tweets = new ArrayList<>(tweetPersistenceService.findTweetsByUserName(user));

            tweets.addAll(followPersistenceService.getFollowedUsersNames(user).stream()
                    .map(tweetPersistenceService::findTweetsByUserName)
                    .flatMap(List::stream)
                    .collect(Collectors.toList()));

            if(tweets.isEmpty()) {
                log.debug("No tweets found for {}'s wall", user);
            } else {
                tweets.sort(Comparator.comparing(Tweet::getCreationDate).reversed());
                tweets.forEach(tweetOutputService::sendWallMessageToOutput);
            }
        }
    }
}
