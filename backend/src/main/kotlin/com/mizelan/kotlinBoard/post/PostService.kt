package com.mizelan.kotlinBoard.post

import javassist.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PostService (val postRepository: PostRepository) {

    fun getAllPosts() : List<Post> = postRepository.findAll().toList()

    fun getPost(id: Long) = postRepository.findByIdOrNull(id)

    fun createPost(request: CreatePostRequest) = postRepository.save(Post(title = request.title, content = request.content))

    fun deletePost(id: Long) = postRepository.deleteById(id)

    fun updatePost(id: Long, request: UpdatePostRequest): Post {
        var entity =
            postRepository.findById(id).orElse(null) ?:
                throw NotFoundException("post `$id` not found.")

        postRepository.save(
                entity.copy(
                        title = request.title,
                        content = request.content,
                        updatedAt = Date()
                ))

        return entity
    }
}

