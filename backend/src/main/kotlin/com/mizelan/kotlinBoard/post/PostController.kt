package com.mizelan.kotlinBoard.post

import com.mizelan.kotlinBoard.user.UserRole
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/post")
class PostController(val postRepository: PostRepository, val postService: PostService) {

    val logger = KotlinLogging.logger {}

    @GetMapping
    fun getAll(page: Int, postCount: Int) : ResponseEntity<Map<String, Any>> {
        require(page > 0)
        require(postCount in 1..100)
        var targetPage = page - 1
        val dataResult = postService.getPosts(
                PageRequest.of(targetPage, postCount, Sort.by(Sort.Direction.DESC, "id")))
        var responseData = mapOf(
                "postList" to dataResult.content,
                "pageInfo" to PaginationInfo(dataResult.number, dataResult.totalPages, 5)) // TODO: countOfPage를 외부 설정값으로 옮기기
        return ResponseEntity(responseData, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun getById(@PathVariable("id") id: Long): ResponseEntity<Post> {
        require(id > 0)
        var result = postService.getPost(id)
        return ResponseEntity(result, HttpStatus.OK)
    }

    //@Secured("ROLE_USER") // TODO: Secured로 메소드 단위 인가하기  
    @PostMapping
    fun create(
            //@AuthenticationPrincipal activeUser: User,
            @Validated @RequestBody request: CreatePostRequest): ResponseEntity<Post> {
        var result = postService.createPost(request)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PutMapping(path = ["/{id}"])
    fun update(@RequestBody request: UpdatePostRequest, @PathVariable("id") id: Long): ResponseEntity<Post> {
        val result = postService.updatePost(id, request)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        require(id > 0)
        postService.deletePost(id)
        return ResponseEntity(HttpStatus.OK)
    }
}