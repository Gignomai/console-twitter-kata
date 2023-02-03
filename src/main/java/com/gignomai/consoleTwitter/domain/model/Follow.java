package com.gignomai.consoleTwitter.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Follow {

    private final String followerUser;
    private final String followedUser;
}
