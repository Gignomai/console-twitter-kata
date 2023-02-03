package com.gignomai.consoleTwitter.infrastructure.repository.adapter;

import com.gignomai.consoleTwitter.domain.model.Follow;
import com.gignomai.consoleTwitter.infrastructure.repository.model.FollowDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLFollowPersistenceServiceAdapterTest {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("tweetPersistenceUnit");
    EntityManager entityManager = factory.createEntityManager();

    private SQLFollowPersistenceServiceAdapter sqlFollowPersistenceServiceAdapter;

    @BeforeEach
    private void setUp() {
        sqlFollowPersistenceServiceAdapter = new SQLFollowPersistenceServiceAdapter();
    }

    @Test
    void shouldSaveFollow() {
        Follow follow = Follow.builder()
                .followerUser("follower")
                .followedUser("followed")
                .build();

        sqlFollowPersistenceServiceAdapter.saveFollow(follow);

        entityManager.getTransaction().begin();
        FollowDb persistedFollow = entityManager.find(FollowDb.class, 1);
        assertNotNull(persistedFollow);
        assertTrue(persistedFollow.getId() > 0);
        assertEquals(follow.getFollowerUser(), persistedFollow.getFollowerUser());
        assertEquals(follow.getFollowedUser(), persistedFollow.getFollowedUser());

        entityManager.remove(persistedFollow);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldGetFollowedUserNames() {
        String followerName = "user1";
        String followedName = "user2";

        entityManager.getTransaction().begin();

        entityManager.persist(FollowDb.builder()
                .followerUser(followerName)
                .followedUser(followedName)
                .build());

        entityManager.getTransaction().commit();

        List<String> usersNames = sqlFollowPersistenceServiceAdapter.getFollowedUsersNames(followerName);
        assertNotNull(usersNames);
        assertEquals(1, usersNames.size());

        entityManager.getTransaction().begin();
        List<?> follows = entityManager.createQuery("SELECT f FROM FollowDb f").getResultList();
        follows.stream()
                .map(follow -> (FollowDb) follow)
                .forEach(follow -> entityManager.remove(follow));
        entityManager.getTransaction().commit();
    }
}