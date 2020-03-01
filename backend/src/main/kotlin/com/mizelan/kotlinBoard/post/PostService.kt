package com.mizelan.kotlinBoard.post

import com.mizelan.kotlinBoard.user.UserEntity
import javassist.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.security.auth.message.AuthException

@Service
@Transactional
class PostService (val postRepository: PostRepository) {

    fun getPosts(pageable: Pageable) : Page<PostEntity> = postRepository.findAll(pageable)

    fun getPost(id: Long) = postRepository.findByIdOrNull(id)

    fun createPost(user: UserEntity, request: CreatePostRequest) =
            postRepository.save(PostEntity(author = user, title = request.title, content = request.content))

    fun deletePost(id: Long) = postRepository.deleteById(id)

    fun updatePost(user: UserEntity, id: Long, request: UpdatePostRequest): PostEntity {
        var entity =
            postRepository.findById(id).orElse(null) ?:
                throw NotFoundException("post `$id` not found.")

        if (user.id != entity.authorId())
            throw AuthException("not enough authority")

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

