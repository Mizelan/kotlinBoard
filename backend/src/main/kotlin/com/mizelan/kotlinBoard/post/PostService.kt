package com.mizelan.kotlinBoard.post

import com.mizelan.kotlinBoard.user.UserEntity
import javassist.NotFoundException
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PostService (val postRepository: PostRepository) {

    fun getPosts(pageable: Pageable) : Page<Post> = postRepository.findAll(pageable)

    fun getPost(id: Long) = postRepository.findByIdOrNull(id)

    fun createPost(user: UserEntity, request: CreatePostRequest) =
            postRepository.save(Post(author = user, title = request.title, content = request.content))

    fun deletePost(id: Long) = postRepository.deleteById(id)

    fun updatePost(id: Long, request: UpdatePostRequest): Post {
        var entity =
            postRepository.findById(id).orElse(null) ?:
                throw NotFoundException("post `$id` not found.")

        postRepository.save(
                entity.copy(
                        title = request.title,
                        content = request.content,
                        createdAt = entity.createdAt,
                        updatedAt = Date()
                ))

        return entity
    }
}

