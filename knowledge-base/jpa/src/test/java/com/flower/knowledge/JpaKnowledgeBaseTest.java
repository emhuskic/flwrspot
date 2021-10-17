package com.flower.knowledge;
import com.flower.knowledge.entity.FlowerEntity;
import com.flower.knowledge.entity.FlowerSightingEntity;
import com.flower.knowledge.entity.UserEntity;
import com.flower.knowledge.entity.UserSightingLikeEntity;
import com.flower.knowledge.model.Flower;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.knowledge.model.UserSightingLike;
import com.flower.knowledge.repository.FlowerRepository;
import com.flower.knowledge.repository.FlowerSightingRepository;
import com.flower.knowledge.repository.UserRepository;
import com.flower.knowledge.repository.UserSightingLikeRepository;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JpaKnowledgeBaseTest {

    @Mock
    private UserRepository userRepository = EasyMock.createMock(UserRepository.class);

    @Mock
    private FlowerRepository flowerRepository = EasyMock.createMock(FlowerRepository.class);

    @Mock
    private FlowerSightingRepository flowerSightingRepository = EasyMock.createMock(FlowerSightingRepository.class);

    @Mock
    private UserSightingLikeRepository likeRepository = EasyMock.createMock(UserSightingLikeRepository.class);

    @TestSubject
    private KnowledgeBase knowledgeBase = new JpaKnowledgeBase(userRepository, flowerRepository, flowerSightingRepository, likeRepository);

    @Test
    void test_whenUserSaved_thenFindByUsername() {
        final UUID userId = UUID.randomUUID();
        final User user = new User(userId, "email@email.ba", "username", "password", Collections.emptyList());
        final UserEntity userEntity = UserEntity.fromDomainModel(user);

        EasyMock.expect(userRepository.save(EasyMock.anyObject(UserEntity.class))).andReturn(userEntity);
        EasyMock.expect(userRepository.findByUsername("username")).andReturn(Optional.of(userEntity));
        EasyMock.replay(userRepository);

        knowledgeBase.save(user);

        assertNotNull(knowledgeBase.findUserByUsername("username"));
        EasyMock.verify(userRepository);
        EasyMock.resetToDefault(userRepository);
    }

    @Test
    void test_listFlowers() {
        final Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
        EasyMock.expect(flowerRepository.findAll(pageable)).andReturn(Page.empty());
        EasyMock.replay(flowerRepository);

        List<Flower> flowers = knowledgeBase.getAllFlowers(1, 10);

        assertNotNull(flowers);
        assertEquals(flowers, Collections.emptyList());
        EasyMock.verify(flowerRepository);
        EasyMock.resetToDefault(flowerRepository);
    }

    @Test
    void test_getFlowerByName() {
        final String flowerName = "flower";
        final Flower flower = new Flower(UUID.randomUUID(),flowerName, "Description", "", Collections.emptyList());
        EasyMock.expect(flowerRepository.findByName(flowerName)).andReturn(Optional.of(FlowerEntity.fromDomainModel(flower)));
        EasyMock.replay(flowerRepository);

        final Flower found = knowledgeBase.findFlowerByName(flowerName).orElse(null);

        assertNotNull(found);
        assertEquals(found.getImageUrl(), flower.getImageUrl());
        assertEquals(found.getDescription(), flower.getDescription());
        assertEquals(found.getName(), flower.getName());
        EasyMock.verify(flowerRepository);
        EasyMock.resetToDefault(flowerRepository);
    }

    @Test
    void test_whenSightingSaved_thenFind() {
        final UUID userId = UUID.randomUUID();
        final UUID flowerId = UUID.randomUUID();
        final UUID sightingId = UUID.randomUUID();
        final User user = new User(userId, "email@email.ba", "username", "password", Collections.emptyList());
        final Flower flower = new Flower(flowerId,"flower" , "Description", "", Collections.emptyList());

        final FlowerSighting flowerSighting = new FlowerSighting(
                sightingId,
                34.5355,
                24.5235,
                "",
                user,
                flower,
                "quote",
                Collections.emptyList()
        );
        final FlowerSightingEntity flowerSightingEntity = FlowerSightingEntity.fromDomainModel(flowerSighting);

        EasyMock.expect(flowerSightingRepository.save(EasyMock.anyObject(FlowerSightingEntity.class))).andReturn(flowerSightingEntity);
        EasyMock.expect(flowerSightingRepository.findById(sightingId)).andReturn(Optional.of(flowerSightingEntity));
        EasyMock.expect(flowerSightingRepository.findAllByFlowerId(flowerId)).andReturn(List.of(flowerSightingEntity));
        EasyMock.expect(flowerSightingRepository.findAllByUserId(userId)).andReturn(List.of(flowerSightingEntity));
        EasyMock.replay(flowerSightingRepository);

        knowledgeBase.saveSighting(flowerSighting);

        assertNotNull(knowledgeBase.findSightingById(sightingId));
        assertNotNull(knowledgeBase.getSightingsForUser(userId));
        assertNotNull(knowledgeBase.getSightingsForFlower(flowerId));
        EasyMock.verify(flowerSightingRepository);
        EasyMock.resetToDefault(flowerSightingRepository);
    }


    @Test
    void test_whenLikeSaved_thenFind() {
        final UUID userId = UUID.randomUUID();
        final UUID flowerId = UUID.randomUUID();
        final UUID sightingId = UUID.randomUUID();
        final UUID likeId = UUID.randomUUID();

        final User user = new User(userId, "email@email.ba", "username", "password", Collections.emptyList());
        final Flower flower = new Flower(flowerId,"flower" , "Description", "", Collections.emptyList());
        final FlowerSighting flowerSighting = new FlowerSighting(
                sightingId,
                34.5355,
                24.5235,
                "",
                user,
                flower,
                "quote",
                Collections.emptyList()
        );

        final UserSightingLike like = new UserSightingLike(likeId, user, flowerSighting);
        final UserSightingLikeEntity likeEntity = UserSightingLikeEntity.fromDomainModel(like);

        EasyMock.expect(likeRepository.save(EasyMock.anyObject(UserSightingLikeEntity.class))).andReturn(likeEntity);
        EasyMock.expect(likeRepository.findById(likeId)).andReturn(Optional.of(likeEntity));
        EasyMock.replay(likeRepository);

        knowledgeBase.saveLike(like);

        assertNotNull(knowledgeBase.findLike(likeId));
        EasyMock.verify(likeRepository);
        EasyMock.resetToDefault(likeRepository);
    }

    @Test
    void test_deleteSighting() {
        final UUID sightingId = UUID.randomUUID();
        flowerSightingRepository.deleteById(EasyMock.anyObject());
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(flowerSightingRepository);

        knowledgeBase.deleteSighting(sightingId);
        EasyMock.verify(flowerSightingRepository);
        EasyMock.resetToDefault(flowerSightingRepository);
    }

    @Test
    void test_deleteLike() {
        final UUID likeId = UUID.randomUUID();
        likeRepository.deleteById(EasyMock.anyObject());
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(likeRepository);

        knowledgeBase.deleteLike(likeId);
        EasyMock.verify(likeRepository);
        EasyMock.resetToDefault(likeRepository);
    }

    @Test
    void test_deleteUserLike() {
        final UUID userId = UUID.randomUUID();
        likeRepository.deleteByUserId(EasyMock.anyObject());
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(likeRepository);

        knowledgeBase.deleteUserLikes(userId);
        EasyMock.verify(likeRepository);
        EasyMock.resetToDefault(likeRepository);
    }

    @Test
    void test_deleteByUserAndId() {
        final UUID likeId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        likeRepository.deleteByIdAndUserId(EasyMock.anyObject(), EasyMock.anyObject());
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(likeRepository);

        knowledgeBase.deleteUserLikes(userId, likeId);
        EasyMock.verify(likeRepository);
        EasyMock.resetToDefault(likeRepository);
    }
}
