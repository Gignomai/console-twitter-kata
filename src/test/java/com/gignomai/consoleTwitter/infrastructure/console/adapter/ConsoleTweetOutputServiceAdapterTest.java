package com.gignomai.consoleTwitter.infrastructure.console.adapter;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleTweetOutputServiceAdapterTest {

    private ConsoleTweetOutputServiceAdapter consoleTweetOutputServiceAdapter;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        consoleTweetOutputServiceAdapter = new ConsoleTweetOutputServiceAdapter();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void shouldPrintReadOutputInSystemOut() {
        Tweet tweet = Tweet.builder()
                .username("userName")
                .message("message")
                .creationDate(Instant.now())
                .build();

        consoleTweetOutputServiceAdapter.sendReadMessageToOutput(tweet);

        assertEquals(String.format("%s (0 seconds ago)", tweet.getMessage()), outContent.toString().trim());
    }

    @Test
    void shouldPrintMinutesReadOutputInSystemOut() {
        Tweet tweet = Tweet.builder()
                .username("userName")
                .message("message")
                .creationDate(Instant.now().minus(2, ChronoUnit.MINUTES))
                .build();

        consoleTweetOutputServiceAdapter.sendReadMessageToOutput(tweet);

        assertEquals(String.format("%s (2 minutes ago)", tweet.getMessage()), outContent.toString().trim());
    }

    @Test
    void shouldPrintHoursReadOutputInSystemOut() {
        Tweet tweet = Tweet.builder()
                .username("userName")
                .message("message")
                .creationDate(Instant.now().minus(2, ChronoUnit.HOURS))
                .build();

        consoleTweetOutputServiceAdapter.sendReadMessageToOutput(tweet);

        assertEquals(String.format("%s (2 hours ago)", tweet.getMessage()), outContent.toString().trim());
    }

    @Test
    void shouldPrintDaysReadOutputInSystemOut() {
        Tweet tweet = Tweet.builder()
                .username("userName")
                .message("message")
                .creationDate(Instant.now().minus(2, ChronoUnit.DAYS))
                .build();

        consoleTweetOutputServiceAdapter.sendReadMessageToOutput(tweet);

        assertEquals(String.format("%s (2 days ago)", tweet.getMessage()), outContent.toString().trim());
    }

    @Test
    void shouldPrintWallOutputInSystemOut() {
        Tweet tweet = Tweet.builder()
                .username("userName")
                .message("message")
                .creationDate(Instant.now())
                .build();

        consoleTweetOutputServiceAdapter.sendWallMessageToOutput(tweet);

        assertEquals(String.format("%s - %s (0 seconds ago)", tweet.getUsername(), tweet.getMessage()),
                outContent.toString().trim());
    }
}
