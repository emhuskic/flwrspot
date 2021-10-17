package com.flower.ws.rest.v1;

import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.knowledge.model.UserSightingLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/like")
public class UserSightingLikeController {
    private final KnowledgeBase knowledgeBase;

    @Autowired
    public UserSightingLikeController(final KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserSightingLike> addLike(@PathVariable("id") final UUID sightingId) {

        final FlowerSighting sighting = this.knowledgeBase.findSightingById(sightingId).orElse(null);
        if (sighting == null) {
            return ResponseEntity.badRequest().build();
        }

        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        final String username = userDetails.getUsername();
        final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

        if (user != null) {
            final UserSightingLike saved = this.knowledgeBase.saveLike(new UserSightingLike(null, user, sighting));
            return ResponseEntity.status(HttpStatus.OK).body(saved);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserSightingLike> addLike(@RequestBody @Valid final FlowerSighting sighting) {
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        final String username = userDetails.getUsername();
        final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

        if (user != null) {
            final UserSightingLike saved = this.knowledgeBase.saveLike(new UserSightingLike(null, user, sighting));
            return ResponseEntity.status(HttpStatus.OK).body(saved);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity deleteLikes() {
        try {
            final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            final String username = userDetails.getUsername();
            final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

            if (user != null) {
                this.knowledgeBase.deleteUserLikes(user.getId());
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLikes(@PathVariable("id") final UUID likeId) {
        try {
            final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            final String username = userDetails.getUsername();
            final User user = this.knowledgeBase.findUserByUsername(username).orElse(null);

            if (user != null && this.knowledgeBase.findLike(likeId).isPresent()) {
                this.knowledgeBase.deleteUserLikes(user.getId(), likeId);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
