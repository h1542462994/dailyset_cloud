package org.tty.dailyset.dailyset_cloud.auth

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes.deviceCodeError
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes.tokenError
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes.userNoExists
import org.tty.dailyset.dailyset_cloud.bean.Responses

@ControllerAdvice
class AuthErrorHandler {
    @ExceptionHandler(AuthError::class)
    @ResponseBody
    fun authErrorHandler(error: AuthError): Responses<Any> {
        return when(error.type) {
            AuthError.AUTH_TYPE_DEVICE_CODE_ERROR -> Responses.fail(ResponseCodes.deviceCodeError, error.message.toString())
            AuthError.AUTH_TYPE_USER_NO_EXISTS -> Responses.fail(ResponseCodes.userNoExists, error.message.toString())
            else -> Responses.fail(ResponseCodes.tokenError, error.message.toString())
        }
    }
}