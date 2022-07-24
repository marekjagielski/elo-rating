package com.elorating.controller;

import com.elorating.model.Feedback;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class FeedbackControllerTest extends BaseControllerTest {

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() throws Exception { }

    @Test
    public void testSend() throws Exception {
        String url = "/api/feedback/send";
        Feedback feedback = new Feedback("test@mail.com", "Test feedback body");
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }
}