package com.devteria.post.controller;

import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.Matchers.not;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test.properties")
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    private PostResponse postResponse;
    private PostRequest postRequest;
    private Post post;

    @BeforeEach
    void setUp() {
        postRequest = PostRequest.builder()
                .content("Only for Testing")
                .build();

        postResponse = PostResponse.builder()
                .content("Only for Testing")
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .userId("User123")
                .content("Only for Testing")
                .build();

        post = Post.builder()
                .content("Only for Testing")
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .userId("User123")
                .content("Only for Testing")
                .build();
    }


    @Test
    @WithMockUser(username = "user", roles = {"user"})
    void createPost_success() throws Exception {

        //Given
        Mockito.when(postService.createPost(postRequest)).thenReturn(postResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(postRequest);

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.content").value(postResponse.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("result.createdDate", not(Instant.now().plus(Duration.ofMinutes(1)))));
        //Then
        log.info("Success");
    }

}