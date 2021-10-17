package com.flower.ws.rest.v1;

import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.knowledge.model.UserSightingLike;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserSightingLikeControllerTest {

    @Mock
    private final KnowledgeBase knowledgeBase = EasyMock.createMock(KnowledgeBase.class);


    private final SecurityContext mockSecurityContext = new SecurityContext() {
        @Override
        public Authentication getAuthentication() {
            return authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {

        }
    };

    @Mock
    private final Authentication authentication = new Authentication() {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.emptyList();
                }

                @Override
                public String getPassword() {
                    return "encoded";
                }

                @Override
                public String getUsername() {
                    return "username";
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        }

        @Override
        public boolean isAuthenticated() {
            return true;
        }

        @Override
        public void setAuthenticated(boolean b) throws IllegalArgumentException {

        }

        @Override
        public String getName() {
            return null;
        }
    };

    @TestSubject
    private final UserSightingLikeController controller = new UserSightingLikeController(knowledgeBase);

    @Test
    public void test_addLike() {
        final UUID sightingId = UUID.randomUUID();
        final User user = new User(UUID.randomUUID(), "email", "username", "encoded", Collections.emptyList());
        final FlowerSighting sighting = EasyMock.createMock(FlowerSighting.class);
        final UserSightingLike like = EasyMock.createMock(UserSightingLike.class);

        EasyMock.expect(knowledgeBase.findUserByUsername(EasyMock.anyString())).andReturn(Optional.of(user));
        EasyMock.expect(knowledgeBase.findSightingById(EasyMock.anyObject(UUID.class))).andReturn(Optional.of(sighting));
        EasyMock.expect(knowledgeBase.saveLike(EasyMock.anyObject(UserSightingLike.class))).andReturn(like);
        EasyMock.replay(knowledgeBase);

        SecurityContextHolder.setContext(mockSecurityContext);
        final ResponseEntity<?> responseEntity = controller.addLike(sightingId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        EasyMock.verify(knowledgeBase);
    }

    @Test
    public void test_deleteUserLikes() {
        final User user = new User(UUID.randomUUID(), "email", "username", "encoded", Collections.emptyList());

        EasyMock.expect(knowledgeBase.findUserByUsername(EasyMock.anyString())).andReturn(Optional.of(user));
        knowledgeBase.deleteUserLikes(EasyMock.anyObject(UUID.class));
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(knowledgeBase);

        SecurityContextHolder.setContext(mockSecurityContext);
        final ResponseEntity<?> responseEntity = controller.deleteLikes();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        EasyMock.verify(knowledgeBase);
    }

    @Test
    public void test_deleteSpecificLike() {
        final User user = new User(UUID.randomUUID(), "email", "username", "encoded", Collections.emptyList());
        final UserSightingLike like = EasyMock.createMock(UserSightingLike.class);

        EasyMock.expect(knowledgeBase.findUserByUsername(EasyMock.anyString())).andReturn(Optional.of(user));
        EasyMock.expect(knowledgeBase.findLike(EasyMock.anyObject(UUID.class))).andReturn(Optional.of(like));
        knowledgeBase.deleteUserLikes(EasyMock.anyObject(UUID.class), EasyMock.anyObject(UUID.class));
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(knowledgeBase);

        SecurityContextHolder.setContext(mockSecurityContext);
        final ResponseEntity<?> responseEntity = controller.deleteLikes(UUID.randomUUID());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        EasyMock.verify(knowledgeBase);
    }
}
