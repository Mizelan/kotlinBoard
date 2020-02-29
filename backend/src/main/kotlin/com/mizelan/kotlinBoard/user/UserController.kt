package com.mizelan.kotlinBoard.user

import com.mizelan.kotlinBoard.exception.RestAPIRequestException
import com.mizelan.kotlinBoard.utils.ApiResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.BindingResult
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
                            .body(ApiResponse("invalid username or password}"))
                }
                else -> throw e
            }
        }
    }

    // TODO: https or http2로 바꾸기. password를 암호화해야 함
    // TODO: 이미 있는 유저일 경우의 응답 메세지 추가
    @PostMapping("/signup")
    fun signUp(
            @RequestBody @Valid param: SignUpRequest,
            result: BindingResult
    ): ResponseEntity<ApiResponse> {

        if (result.hasErrors())
            throw RestAPIRequestException(result.toString(), HttpStatus.BAD_REQUEST)

        return try {
            userService.preCreateUser(param.userId, param.passWd, param.confirmPassWd)
            userService.createUser(param.userId, param.passWd)
            ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse(message = "success"))
        } catch (e: java.lang.Exception) {
            // TODO: 예외 나눠서 처리하기
            //log.error(e.message, e)
            ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse("internal error: ${e.toString()}"))
        }
    }
}