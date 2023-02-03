package com.gignomai.consoleTwitter.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Tweet {
    private final String username;
    private final String message;
    private final Instant creationDate;
}
