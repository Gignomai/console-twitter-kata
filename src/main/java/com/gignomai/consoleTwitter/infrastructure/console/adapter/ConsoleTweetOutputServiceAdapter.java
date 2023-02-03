package com.gignomai.consoleTwitter.infrastructure.console.adapter;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetOutputService;

import java.time.Duration;
import java.time.Instant;

public class ConsoleTweetOutputServiceAdapter implements TweetOutputService {

    @Override
    public void sendReadMessageToOutput(Tweet tweet) {
        System.out.println(getProcessedMessage(tweet));
    }

    @Override
    public void sendWallMessageToOutput(Tweet tweet) {
        System.out.println(getWallProcessedMessage(tweet));
    }

    private String getWallProcessedMessage(Tweet tweet) {
        return String.format("%s - %s %s", tweet.getUsername(), tweet.getMessage(), timeFromNow(tweet.getCreationDate()));
    }

    private String getProcessedMessage(Tweet tweet) {
        return String.format("%s %s", tweet.getMessage(), timeFromNow(tweet.getCreationDate()));
    }

    private String timeFromNow(Instant instant) {
        String result;
        long seconds = Duration.between(instant, Instant.now()).getSeconds();

        if (seconds < 60) {
            result = String.format("(%d seconds ago)", seconds);
        } else if (seconds < 3600) {
            result = String.format("(%d minutes ago)", seconds / 60);
        } else if (seconds < 86400) {
            result = String.format("(%d hours ago)", seconds / 3600);
        } else {
            result = String.format("(%d days ago)", seconds / 86400);
        }
        return result;
    }
}
