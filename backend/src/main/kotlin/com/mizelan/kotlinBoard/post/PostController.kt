package com.mizelan.kotlinBoard.post

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/post")
class PostController(val postRepository: PostRepository) {
    @GetMapping
    fun getAll() : List<Post> = postRepository.findAll()

    @GetMapping(path = ["/{id}"])
    fun getById(@PathVariable("id") todoId: Long): Post? {
        return postRepository.findById(todoId).orElse(null)
    }

    @PostMapping
    fun create(@RequestBody todo: Post): Post {
        postRepository.save(todo)
        return todo
    }

    @PutMapping(path = ["/{id}"])
    fun updateTodo(@RequestBody post: Post, @PathVariable("id") id: Long): ResponseEntity<Unit> {
        var target = postRepository.findById(id).get()
        target = post.copy()
        postRepository.save(target)
        return ResponseEntity<Unit>(HttpStatus.OK)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable("id") id: Long){
        postRepository.deleteById(id)
    }
}