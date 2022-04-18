/**
 * create at 2022/4/17
 * @author h1542462994
 * exception class authError
 */

package org.tty.dailyset.dailyset_cloud.auth

/**
 * error class occurred in [AuthInterceptor], handled by [AuthErrorHandler]
 */
class AuthError(val type: Int, message: String): RuntimeException(message) {
    companion object {
        const val AUTH_TYPE_TOKEN_ERROR = 0
        const val AUTH_TYPE_USER_NO_EXISTS = 1
        const val AUTH_TYPE_DEVICE_CODE_ERROR = 2
    }
}
