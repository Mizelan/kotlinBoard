package com.mizelan.kotlinBoard.post

import com.google.gson.Gson
import com.mizelan.kotlinBoard.user.UserEntity
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

inline fun <reified T: Any> mock(): T = Mockito.mock(T::class.java)


@SpringBootTest
@AutoConfigureMockMvc
class PostEntityControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var postService: PostService

    @Test
    fun getAll() {
        val postPage = PageImpl<PostEntity>(
                listOf(
                        PostEntity(1,null,"one", "one"),
                        PostEntity(2,null,"two", "two"),
                        PostEntity(3,null,"three", "three")
                ))
        val pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"))
        given(postService.getPosts(pageable))
                .willReturn(postPage)

        mockMvc.perform(get("/api/post")
                    .param("page", pageable.pageNumber.toString())
                    .param("postCount", pageable.pageSize.toString()))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.postList.length()").value(3))
                .andDo(print())

        then(postService).should(atMostOnce()).getPosts(pageable)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    fun getById() {
        given(postService.getPost(1))
                .willReturn(PostEntity(1, null,"one", "one"));

        mockMvc.perform(get("/api/post/1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(1))
                .andExpect(jsonPath("\$.title").value("one"))
                .andExpect(jsonPath("\$.content").value("one"))
                .andDo(print())

        then(postService).should(atMostOnce()).getPost(1)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun create() {
        val user: UserEntity = mock()
        val request = CreatePostRequest("title", "content")
        given(postService.createPost(user, request))
                .willReturn(PostEntity(123, user, request.title, request.content));

        mockMvc.perform(
                    post("/api/post")
                        .header("Content-type","application/json")
                        .content(Gson().toJson(CreatePostRequest("title", "content"))))

                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(123))
                .andExpect(jsonPath("\$.title").value("title"))
                .andExpect(jsonPath("\$.content").value("content"))
                .andDo(print())

        then(postService).should(atMostOnce()).createPost(user, request)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun update() {
        val user: UserEntity = mock()
        val request = UpdatePostRequest("title", "content")
        given(postService.updatePost(user, 123, request))
                .willReturn(PostEntity(123, user, request.title, request.content));

        mockMvc.perform(
                put("/api/post/123")
                        .header("Content-type","application/json")
                        .content(Gson().toJson(CreatePostRequest("title", "content"))))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(123))
                .andExpect(jsonPath("\$.title").value("title"))
                .andExpect(jsonPath("\$.content").value("content"))
                .andDo(print())

        then(postService).should(atMostOnce()).updatePost(user, 123, request)
        then(postService).shouldHaveNoMoreInteractions()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun delete() {
        willDoNothing().given(postService).deletePost(123)

        mockMvc.perform(delete("/api/post/123"))
                .andExpect(status().isOk)
                .andDo(print())

        then(postService).should(atMostOnce()).deletePost(123)
        then(postService).shouldHaveNoMoreInteractions()
    }
}