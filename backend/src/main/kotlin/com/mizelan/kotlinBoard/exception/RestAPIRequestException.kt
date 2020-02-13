package com.mizelan.kotlinBoard.exception

import org.springframework.http.HttpStatus

class RestAPIRequestException(
        message: String? = null
) : Throwable(message) {
    var status: HttpStatus = HttpStatus.OK
        private set

    constructor(msg: String?, status: HttpStatus) : this(msg) {
        this.status = status
    }
}