package com.mizelan.kotlinBoard.post

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PostService (val postRepository: PostRepository) {

    fun getAllPosts() : List<Post> = postRepository.findAll().toList()

    fun getPost(id: Long) = postRepository.findByIdOrNull(id)

    fun createPost(request: CreatePostRequest) = postRepository.save(Post(request.title, request.content))

    fun deletePost(id: Long) = postRepository.deleteById(id)

    fun updatePost(id: Long,  request: UpdatePostRequest) =
        postRepository.save(
                postRepository.findById(id).get().apply {
                    title = request.title
                    content = request.content
                    updatedAt = Date()
                })
}

