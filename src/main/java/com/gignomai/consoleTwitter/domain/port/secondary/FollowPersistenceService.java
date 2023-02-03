package com.gignomai.consoleTwitter.domain.port.secondary;

import com.gignomai.consoleTwitter.domain.model.Follow;

import java.util.List;

public interface FollowPersistenceService {


    void saveFollow(Follow follow);

    List<String> getFollowedUsersNames(String followerUserName);
}
