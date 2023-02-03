package com.gignomai.consoleTwitter.infrastructure.repository.adapter;

import com.gignomai.consoleTwitter.domain.model.Tweet;
import com.gignomai.consoleTwitter.domain.port.secondary.TweetPersistenceService;
import com.gignomai.consoleTwitter.infrastructure.repository.model.TweetDb;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

public class SQLTweetPersistenceServiceAdapter implements TweetPersistenceService {

    private final EntityManager entityManager;

    public SQLTweetPersistenceServiceAdapter() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tweetPersistenceUnit");
        entityManager = factory.createEntityManager();
    }

    @Override
    public void saveTweet(Tweet tweet) {
        entityManager.getTransaction().begin();
        entityManager.persist(toTweetDb(tweet));
        entityManager.getTransaction().commit();
    }

    private TweetDb toTweetDb(Tweet tweet) {
        return TweetDb.builder()
                .username(tweet.getUsername())
                .message(tweet.getMessage())
                .creationDate(tweet.getCreationDate())
                .build();
    }

    @Override
    public List<Tweet> findTweetsByUserName(String userName) {
        entityManager.getTransaction().begin();

        List<?> tweets = entityManager.createQuery("SELECT t FROM TweetDb t WHERE t.username = :username")
                .setParameter("username", userName)
                .getResultList();
        entityManager.getTransaction().commit();

        return tweets.stream()
                .map(tweet -> toTweet((TweetDb) tweet))
                .collect(Collectors.toList());
    }

    private Tweet toTweet(TweetDb tweetDb) {
        return Tweet.builder()
                .username(tweetDb.getUsername())
                .message(tweetDb.getMessage())
                .creationDate(tweetDb.getCreationDate())
                .build();
    }
}
