package com.gignomai.consoleTwitter.domain.port.secondary;

import com.gignomai.consoleTwitter.domain.model.Tweet;

public interface TweetOutputService {
    void sendReadMessageToOutput(Tweet tweet);

    void sendWallMessageToOutput(Tweet capture);
}
