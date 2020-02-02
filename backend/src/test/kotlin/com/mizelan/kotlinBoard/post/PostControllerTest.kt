package com.mizelan.kotlinBoard.post

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var postService: PostService

    @Test
    fun getAll() {
        given(postService.getAllPosts())
            .willReturn(listOf(
                Post(1,"one", "one"),
                Post(2,"two", "two"),
                Post(3,"three", "three")));

        mockMvc.perform(get("/post"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.length()").value(3))
                .andDo(print())

        then(postService).should(atMostOnce()).getAllPosts()
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    fun getById() {
        given(postService.getPost(1))
                .willReturn(Post(1,"one", "one"));

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(1))
                .andExpect(jsonPath("\$.title").value("one"))
                .andExpect(jsonPath("\$.content").value("one"))
                .andDo(print())

        then(postService).should(atMostOnce()).getPost(1)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    fun create() {
        val request = CreatePostRequest("title", "content")
        given(postService.createPost(request))
                .willReturn(Post(123, request.title, request.content));

        mockMvc.perform(
                    post("/post")
                        .header("Content-type","application/json")
                        .content(Gson().toJson(CreatePostRequest("title", "content"))))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(123))
                .andExpect(jsonPath("\$.title").value("title"))
                .andExpect(jsonPath("\$.content").value("content"))
                .andDo(print())

        then(postService).should(atMostOnce()).createPost(request)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    fun update() {
        val request = UpdatePostRequest("title", "content")
        given(postService.updatePost(123, request))
                .willReturn(Post(123, request.title, request.content));

        mockMvc.perform(
                put("/post/123")
                        .header("Content-type","application/json")
                        .content(Gson().toJson(CreatePostRequest("title", "content"))))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(123))
                .andExpect(jsonPath("\$.title").value("title"))
                .andExpect(jsonPath("\$.content").value("content"))
                .andDo(print())

        then(postService).should(atMostOnce()).updatePost(123, request)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    fun delete() {
        willDoNothing().given(postService).deletePost(123)

        mockMvc.perform(delete("/post/123"))
                .andExpect(status().isOk)
                .andDo(print())

        then(postService).should(atMostOnce()).deletePost(123)
        then(postService).shouldHaveNoMoreInteractions()
    }
}