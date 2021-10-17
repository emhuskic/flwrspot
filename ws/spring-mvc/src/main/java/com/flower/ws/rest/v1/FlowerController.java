package com.flower.ws.rest.v1;

import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.Flower;
import com.flower.ws.rest.params.PaginationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flower")
public class FlowerController {
    private final KnowledgeBase knowledgeBase;

    @Autowired
    public FlowerController(final KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Flower>> getFlowers(final PaginationParams paginationParams) {
        try {
            final List<Flower> flowers = this.knowledgeBase.getAllFlowers(paginationParams.getPageNumber(), paginationParams.getPageSize());
            return ResponseEntity.status(HttpStatus.OK).body(flowers);
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
