package com.flower.ws.rest.v1;

import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.Flower;
import com.flower.ws.rest.params.PaginationParams;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

public class FlowerControllerTest {
    @Mock
    private final KnowledgeBase knowledgeBase = EasyMock.createMock(KnowledgeBase.class);

    @TestSubject
    private final FlowerController flowerController = new FlowerController(knowledgeBase);

    @Test
    public void listFlowers() {
        EasyMock.expect(knowledgeBase.getAllFlowers(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(Collections.emptyList());
        EasyMock.replay(knowledgeBase);
        final ResponseEntity<List<Flower>> flowerList = flowerController.getFlowers(new PaginationParams(1, 100));
        EasyMock.verify(knowledgeBase);

        assertEquals(HttpStatus.OK, flowerList.getStatusCode());
        assertEquals(Collections.emptyList(), flowerList.getBody());
    }
}
