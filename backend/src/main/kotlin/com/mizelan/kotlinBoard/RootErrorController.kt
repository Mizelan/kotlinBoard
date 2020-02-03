package com.mizelan.kotlinBoard

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RootErrorController : ErrorController{

    @GetMapping("/error")
    fun redirectRoot() = "index.html"

    override fun getErrorPath() = "/error"
}