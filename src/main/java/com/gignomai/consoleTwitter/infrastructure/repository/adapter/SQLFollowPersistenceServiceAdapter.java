package com.gignomai.consoleTwitter.infrastructure.repository.adapter;

import com.gignomai.consoleTwitter.domain.model.Follow;
import com.gignomai.consoleTwitter.domain.port.secondary.FollowPersistenceService;
import com.gignomai.consoleTwitter.infrastructure.repository.model.FollowDb;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

public class SQLFollowPersistenceServiceAdapter implements FollowPersistenceService {

    private final EntityManager entityManager;

    public SQLFollowPersistenceServiceAdapter() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("tweetPersistenceUnit");
        entityManager = factory.createEntityManager();
    }

    @Override
    public void saveFollow(Follow follow) {
        entityManager.getTransaction().begin();
        entityManager.persist(toFollowDb(follow));
        entityManager.getTransaction().commit();
    }

    private FollowDb toFollowDb(Follow follow) {
        return FollowDb.builder()
                .followerUser(follow.getFollowerUser())
                .followedUser(follow.getFollowedUser())
                .build();
    }

    @Override
    public List<String> getFollowedUsersNames(String userName) {
        entityManager.getTransaction().begin();

        List<?> followedUsers = entityManager
                .createQuery("SELECT f FROM FollowDb f WHERE f.followerUser = :username")
                .setParameter("username", userName)
                .getResultList();
        entityManager.getTransaction().commit();

        return followedUsers.stream()
                .map(follow -> toFollow((FollowDb) follow))
                .map(Follow::getFollowedUser)
                .collect(Collectors.toList());
    }

    private Follow toFollow(FollowDb follow) {
        return Follow.builder()
                .followerUser(follow.getFollowerUser())
                .followedUser(follow.getFollowedUser())
                .build();
    }
}
