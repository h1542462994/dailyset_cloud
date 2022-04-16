package org.tty.dailyset.dailyset_cloud.auth

class AuthError(val type: Int, message: String): RuntimeException(message) {
    companion object {
        const val AUTH_TYPE_TOKEN_ERROR = 0
        const val AUTH_TYPE_USER_NO_EXISTS = 1
        const val AUTH_TYPE_DEVICE_CODE_ERROR = 2
    }
}
