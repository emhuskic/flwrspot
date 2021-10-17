package com.flower.ws.rest.v1;

import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.Flower;
import com.flower.ws.auth.service.AuthService;
import com.flower.ws.rest.params.PaginationParams;
import org.easymock.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

@ExtendWith(EasyMockExtension.class)
@AutoConfigureWebMvc
public class FlowerControllerTest {

    private static final String URL = "http://localhost:8080/api/v1";
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Mock
    private KnowledgeBase knowledgeBase;

    @TestSubject
    private FlowerController flowerController = new FlowerController(knowledgeBase);

    @Test
    public void listFlowers() {
        EasyMock.expect((knowledgeBase.getAllFlowers(1, 100))).andReturn(Collections.emptyList());
        EasyMock.replay(knowledgeBase);

        ResponseEntity<List> entity = this.restTemplate
                .getForEntity(EasyMock.eq(URL + "/flower/list"), List.class);

        EasyMock.verify(knowledgeBase);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(Collections.emptyList(), entity.getBody());

    }

}
