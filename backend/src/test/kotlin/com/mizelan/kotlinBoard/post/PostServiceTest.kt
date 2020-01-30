package com.mizelan.kotlinBoard.post

import javassist.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class PostServiceTest(
        @Autowired val postRepository: PostRepository) {

    lateinit var postService: PostService

    @BeforeEach
    fun setUp() {
        postService = PostService(postRepository)
    }

    @Test
    fun getAllPosts() {
        val created1 = postRepository.save(Post(title = "title1", content = "content1"))
        val created2 = postRepository.save(Post(title = "title2", content = "content2"))

        val actual = postService.getAllPosts()
        assertEquals(2, actual.count())
        assertEquals("title1", actual[0].title)
        assertEquals("title2", actual[1].title)
    }

    @Test
    fun getPost() {
        val created = postRepository.save(Post(title = "test-title", content = "test-content"))

        val actual = postService.getPost(created.id!!)
        assertNotNull(actual!!.id)
        assertEquals("test-title", actual.title)
        assertEquals("test-content", actual.content)
    }

    @Test
    fun createPost() {
        var actual =
                postService.createPost(CreatePostRequest("test-title", "test-content"))

        assertNotNull(actual!!.id)
        assertEquals("test-title", actual.title)
        assertEquals("test-content", actual.content)
    }

    @Test
    fun deletePost() {
        val created = postRepository.save(Post(title = "test-title", content = "test-content"))

        postService.deletePost(created.id!!)

        assertEquals(0, postRepository.count())
    }

    @Test
    fun updatePost() {
        val created = postRepository.save(Post(title = "test-title", content = "test-content"))

        postService.updatePost(created.id!!, UpdatePostRequest(title = "new-title", content = "new-content"))

        val actual = postService.getPost(created.id!!)
        assertNotNull(actual!!.id)
        assertEquals("new-title", actual.title)
        assertEquals("new-content", actual.content)
    }

    @Test
    fun `updatePost(), id 찾지 못함`() {
        assertThrows(NotFoundException::class.java) {
            postService.updatePost(1234, UpdatePostRequest("", ""))
        }
    }
}