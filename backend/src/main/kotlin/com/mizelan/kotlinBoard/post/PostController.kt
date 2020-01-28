package com.mizelan.kotlinBoard.post

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController(val postRepository: PostRepository, val postService: PostService) {
    @GetMapping
    fun getAll() : ResponseEntity<List<Post>> {
        var result = postService.getAllPosts()
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun getById(@PathVariable("id") id: Long): ResponseEntity<Post> {
        var result = postService.getPost(id)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PostMapping
    fun create(@RequestBody request: CreatePostRequest): ResponseEntity<Post> {
        var result = postService.createPost(request)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PutMapping(path = ["/{id}"])
    fun updateTodo(@RequestBody request: UpdatePostRequest, @PathVariable("id") id: Long): ResponseEntity<Unit> {
        postService.updatePost(id, request)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        postService.deletePost(id)
        return ResponseEntity(HttpStatus.OK)
    }
}