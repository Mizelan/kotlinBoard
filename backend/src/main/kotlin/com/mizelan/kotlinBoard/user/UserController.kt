package com.mizelan.kotlinBoard.user

import com.mizelan.kotlinBoard.exception.AlreadyRegisteredUsernameException
import com.mizelan.kotlinBoard.exception.ConfirmPasswordNotMatchedException
import com.mizelan.kotlinBoard.utils.ApiResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController {

    val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/login")
    fun userLogin(@RequestBody @Valid request: LoginRequest): ResponseEntity<ApiResponse> {

        return try {
            val userDetails = userService.getUserDetails(request.username, request.password)
            val token = userService.generatorToken(userDetails)
            ResponseEntity
                    .ok()
                    .body(ApiResponse(message = "success", data = token))
        } catch (e: Exception) {
            when (e) {
                is UsernameNotFoundException,
                is BadCredentialsException -> {
                    ResponseEntity
                            .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                            .body(ApiResponse("invalid username or password"))
                }
                else -> throw e
            }
        }
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid param: SignUpRequest
    ): ResponseEntity<ApiResponse> {
        return try {
            userService.validateCreateUserRequest(param.username, param.password, param.confirmPassword)
            userService.createUser(param.username, param.password)
            val userDetails = userService.getUserDetails(param.username, param.password)
            val token = userService.generatorToken(userDetails)

            ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse(message = "success", data = token))
        } catch (e: AlreadyRegisteredUsernameException) {
            badRequest().body(ApiResponse("Already Registered Username"))
        } catch (e: ConfirmPasswordNotMatchedException) {
            badRequest().body(ApiResponse("Confirm Password Not Matched"))
        }
    }
}