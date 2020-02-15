package com.mizelan.kotlinBoard.user

import com.mizelan.kotlinBoard.exception.RestAPIRequestException
import com.mizelan.kotlinBoard.utils.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/login")
    fun userLogin(
            @Valid request: LoginRequest,
            result: BindingResult
    ): ResponseEntity<ApiResponse> {

        // TODO: interceptor로 뺄 수 없을까?
        if (result.hasErrors())
            throw RestAPIRequestException(result.toString(), HttpStatus.BAD_REQUEST)

        return try {
            val userDetails = userService.getUserDetails(request.userId, request.passWd)
            val token = userService.generatorToken(userDetails)
            ResponseEntity
                    .ok()
                    .body(ApiResponse(message = "success",data = token))
        } catch (e: Exception) {
            // TODO: 암호 틀렸을 떄 예외 따로 처리해 응답하기
            // TODO: 예외 나눠서 처리하기
            //log.error(e.message, e)
            ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse("internal error: ${e.toString()}"))
        }
    }

    // TODO: https or http2로 바꾸기. password를 암호화해야 함
    // TODO: 이미 있는 유저일 경우의 응답 메세지 추가
    @PostMapping("/signup")
    fun signUp(
            param: @Valid SignUpRequest,
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