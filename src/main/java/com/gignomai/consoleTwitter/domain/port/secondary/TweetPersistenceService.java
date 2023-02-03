package com.gignomai.consoleTwitter.domain.port.secondary;

import com.gignomai.consoleTwitter.domain.model.Tweet;

import java.util.List;

public interface TweetPersistenceService {

    void saveTweet(Tweet tweet);

    List<Tweet> findTweetsByUserName(String userName);

}
