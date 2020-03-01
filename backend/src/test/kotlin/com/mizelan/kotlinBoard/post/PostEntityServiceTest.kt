package com.mizelan.kotlinBoard.post

import com.mizelan.kotlinBoard.user.UserEntity
import javassist.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest

@DataJpaTest
class PostEntityServiceTest(
        @Autowired val postRepository: PostRepository) {

    lateinit var postService: PostService

    @BeforeEach
    fun setUp() {
        postService = PostService(postRepository)
    }

    @Test
    fun getAllPosts() {
        val user: UserEntity = mock()
        postRepository.save(PostEntity(author = user, title = "title1", content = "content1"))
        postRepository.save(PostEntity(author = user, title = "title2", content = "content2"))

        val pageable = PageRequest.of(0, 10)
        val actual = postService.getPosts(pageable).toList()
        assertEquals(2, actual.count())
        assertEquals("title1", actual[0].title)
        assertEquals("title2", actual[1].title)
    }

    @Test
    fun getPost() {
        val user: UserEntity = mock()
        val created = postRepository.save(PostEntity(author = user, title = "test-title", content = "test-content"))

        val actual = postService.getPost(created.id!!)
        assertNotNull(actual!!.id)
        assertEquals("test-title", actual.title)
        assertEquals("test-content", actual.content)
    }

    @Test
    fun createPost() {
        val user: UserEntity = mock()
        var actual =
                postService.createPost(user, CreatePostRequest("test-title", "test-content"))

        assertNotNull(actual.id)
        assertEquals("test-title", actual.title)
        assertEquals("test-content", actual.content)
    }

    @Test
    fun deletePost() {
        val user: UserEntity = mock()
        val created = postRepository.save(PostEntity(author = user,  title = "test-title", content = "test-content"))

        postService.deletePost(created.id!!)

        assertEquals(0, postRepository.count())
    }

    @Test
    fun updatePost() {
        val user: UserEntity = mock()
        val created = postRepository.save(PostEntity(author = user, title = "test-title", content = "test-content"))

        postService.updatePost(user, created.id!!, UpdatePostRequest(title = "new-title", content = "new-content"))

        val actual = postService.getPost(created.id!!)
        assertNotNull(actual!!.id)
        assertEquals("new-title", actual.title)
        assertEquals("new-content", actual.content)
    }

    @Test
    fun `updatePost(), id 찾지 못함`() {
        val user: UserEntity = mock()
        assertThrows(NotFoundException::class.java) {
            postService.updatePost(user, 1234, UpdatePostRequest("", ""))
        }
    }
}