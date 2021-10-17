package com.flower.ws.rest.v1;

import com.flower.external.quotes.QuoteManager;
import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.Flower;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.ws.auth.params.FlowerSightingParams;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FlowerSightingControllerTest {

    @Mock
    private final KnowledgeBase knowledgeBase = EasyMock.createMock(KnowledgeBase.class);

    @Mock
    private final QuoteManager quoteManager = EasyMock.createMock(QuoteManager.class);

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
    private final FlowerSightingController flowerSightingController = new FlowerSightingController(knowledgeBase, quoteManager);

    @Test
    public void test_getSightings() {
        final UUID flowerId = UUID.randomUUID();
        EasyMock.expect(knowledgeBase.getSightingsForFlower(EasyMock.anyObject(UUID.class))).andReturn(Collections.emptyList());
        EasyMock.replay(knowledgeBase);

        final ResponseEntity<List<FlowerSighting>> response = flowerSightingController.getSightings(flowerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        EasyMock.verify(knowledgeBase);
        EasyMock.resetToDefault(knowledgeBase);
    }

    @Test
    public void test_addSighting() {
        final User user = new User(UUID.randomUUID(), "email", "username", "encoded", Collections.emptyList());
        final FlowerSightingParams params = EasyMock.createMock(FlowerSightingParams.class);
        final FlowerSighting flowerSighting = EasyMock.createMock(FlowerSighting.class);

        EasyMock.expect(params.getLatitude()).andReturn(23.55);
        EasyMock.expect(params.getLongitude()).andReturn(54.33);
        EasyMock.expect(params.getImageUrl()).andReturn("");
        EasyMock.expect(params.getFlower()).andReturn(EasyMock.createMock(Flower.class));

        EasyMock.expect(knowledgeBase.findUserByUsername(EasyMock.anyString())).andReturn(Optional.of(user));
        EasyMock.expect(knowledgeBase.saveSighting(EasyMock.anyObject(FlowerSighting.class))).andReturn(flowerSighting);
        EasyMock.expect(quoteManager.quoteOfTheDay()).andReturn("Quote of the day");

        EasyMock.replay(knowledgeBase, quoteManager, params, flowerSighting);

        SecurityContextHolder.setContext(mockSecurityContext);
        final ResponseEntity<FlowerSighting> responseEntity = flowerSightingController.addSighting(params);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        EasyMock.verify(knowledgeBase, quoteManager, params, flowerSighting);
        EasyMock.resetToDefault(knowledgeBase, quoteManager, params, flowerSighting);
    }

    @Test
    public void test_deleteSightings() {
        final User user = new User(UUID.randomUUID(), "email", "username", "encoded", Collections.emptyList());

        EasyMock.expect(knowledgeBase.findUserByUsername(EasyMock.anyString())).andReturn(Optional.of(user));
        knowledgeBase.deleteUserSightings(EasyMock.anyObject(UUID.class));
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(knowledgeBase);

        SecurityContextHolder.setContext(mockSecurityContext);
        final ResponseEntity<?> responseEntity = flowerSightingController.deleteSightings();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        EasyMock.verify(knowledgeBase);
        EasyMock.resetToDefault(knowledgeBase);
    }

    @Test
    public void test_deleteSpecificUserSighting() {
        final User user = new User(UUID.randomUUID(), "email", "username", "encoded", Collections.emptyList());

        EasyMock.expect(knowledgeBase.findUserByUsername(EasyMock.anyString())).andReturn(Optional.of(user));
        knowledgeBase.deleteUserSightings(EasyMock.anyObject(UUID.class), EasyMock.anyObject(UUID.class));
        EasyMock.expectLastCall().andVoid().once();

        EasyMock.replay(knowledgeBase);

        SecurityContextHolder.setContext(mockSecurityContext);
        final ResponseEntity<?> responseEntity = flowerSightingController.deleteSightings(UUID.randomUUID());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        EasyMock.verify(knowledgeBase);
        EasyMock.resetToDefault(knowledgeBase);
    }
}
