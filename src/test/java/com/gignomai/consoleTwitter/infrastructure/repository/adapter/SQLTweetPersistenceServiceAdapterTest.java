package com.gignomai.consoleTwitter.infrastructure.repository.adapter;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.infrastructure.repository.model.TweetDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SQLTweetPersistenceServiceAdapterTest {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("tweetPersistenceUnit");
    EntityManager entityManager = factory.createEntityManager();

    private SQLTweetPersistenceServiceAdapter sqlTweetPersistenceServiceAdapter;

    @BeforeEach
    public void setUp() {
        sqlTweetPersistenceServiceAdapter = new SQLTweetPersistenceServiceAdapter();
    }

    @Test
    void shouldSaveTweet() {
        Tweet tweet = Tweet.builder()
                .username("userName")
                .message("message")
                .creationDate(Instant.now())
                .build();

        sqlTweetPersistenceServiceAdapter.saveTweet(tweet);

        entityManager.getTransaction().begin();
        TweetDb persistedTweet = entityManager.find(TweetDb.class, 1);
        assertNotNull(persistedTweet);
        assertTrue(persistedTweet.getId() > 0);
        assertEquals(tweet.getUsername(), persistedTweet.getUsername());
        assertEquals(tweet.getMessage(), persistedTweet.getMessage());
        assertEquals(tweet.getCreationDate().truncatedTo(MILLIS), persistedTweet.getCreationDate().truncatedTo(MILLIS));

        entityManager.remove(persistedTweet);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldGetTweetsFromUser() {
        String userName = "user1";
        entityManager.getTransaction().begin();
        entityManager.persist(TweetDb.builder()
                .username(userName)
                .message("message1")
                .creationDate(Instant.now())
                .build());
        entityManager.persist(TweetDb.builder()
                .username("user1")
                .message("message2")
                .creationDate(Instant.now())
                .build());
        entityManager.getTransaction().commit();

        List<Tweet> tweets = sqlTweetPersistenceServiceAdapter.findTweetsByUserName(userName);
        assertNotNull(tweets);
        assertEquals(2, tweets.size());

        entityManager.getTransaction().begin();
        List<?> persistedTweets = entityManager.createQuery("SELECT t FROM TweetDb t WHERE t.username = :username")
                .setParameter("username", userName)
                .getResultList();
        persistedTweets.stream()
                .map(tweet -> (TweetDb) tweet)
                .forEach(tweet -> entityManager.remove(tweet));
        entityManager.getTransaction().commit();
    }
}