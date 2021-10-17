package com.flower.ws.rest.v1;

import com.flower.external.quotes.QuoteManager;
import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.ws.auth.params.FlowerSightingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sighting")
public class FlowerSightingController {
    private final KnowledgeBase knowledgeBase;
    private final QuoteManager quoteManager;

    @Autowired
    public FlowerSightingController(final KnowledgeBase knowledgeBase,
                                    final QuoteManager quoteManager) {
        this.knowledgeBase = knowledgeBase;
        this.quoteManager = quoteManager;
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<FlowerSighting>> getSightings(@PathVariable("id") final UUID flowerId) {
        try {
            final List<FlowerSighting> flowers = this.knowledgeBase.getSightingsForFlower(flowerId);
            return ResponseEntity.status(HttpStatus.OK).body(flowers);
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<FlowerSighting> addSighting(@RequestBody @Valid final FlowerSightingParams params) {
        SecurityContext context = SecurityContextHolder.getContext();
        final UserDetails userDetails = (UserDetails) context
                .getAuthentication()
                .getPrincipal();
        final String username = userDetails.getUsername();
        final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

        if (user != null) {
            final String quote = this.quoteManager.quoteOfTheDay();

            if (quote == null) {
                // Quote could not be obtained, so we should not save FlowerSighting
                return ResponseEntity.internalServerError().build();
            }

            final FlowerSighting flowerSighting = new FlowerSighting(null,
                    params.getLongitude(),
                    params.getLatitude(),
                    params.getImageUrl(),
                    user,
                    params.getFlower(),
                    quote,
                    Collections.emptyList()
            );

            final FlowerSighting saved = this.knowledgeBase.saveSighting(flowerSighting);
            return ResponseEntity.status(HttpStatus.OK).body(saved);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity deleteSightings() {
        try {
            final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            final String username = userDetails.getUsername();
            final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

            if (user != null) {
                this.knowledgeBase.deleteUserSightings(user.getId());
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSightings(@PathVariable("id") final UUID sightingId) {
        try {
            final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            final String username = userDetails.getUsername();
            final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

            if (user != null) {
                this.knowledgeBase.deleteUserSightings(user.getId(), sightingId);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
