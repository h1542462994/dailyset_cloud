/**
 * create at 2022/4/16
 * author: h1542462994
 * response entity used in project.
 */

package org.tty.dailyset.dailyset_cloud.bean
import kotlinx.serialization.Serializable

/**
 * data class of response
 */
@Serializable
data class Responses<T>(
    val code: Int, // data
    val message: String, // message
    val data: T? // data
) {
    companion object {
        /**
         * default fail response entity.
         */
        fun <T> fail(code: Int = ResponseCodes.fail, message: String = "内部错误", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * default success response entity.
         */
        fun <T> ok(code: Int = ResponseCodes.success, message: String = "请求成功", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * default fail response entity in arg error.
         */
        fun <T> argError(message: String = "参数错误", data: T? = null): Responses<T>
            = Responses(ResponseCodes.argError, message, data)

        /**
         * user no exist response entity.
         */
        fun <T> userNoExist(): Responses<T> {
            return fail(ResponseCodes.userNoExists, "用户不存在")
        }

        /**
         * password error response entity.
         */
        fun <T> passwordError(): Responses<T> {
            return fail(ResponseCodes.passwordError, "密码错误")
        }

        /**
         * token error response entity.
         */
        fun <T> tokenError(message: String = "token错误"): Responses<T> {
            return fail(ResponseCodes.tokenError, message)
        }

        fun <T> ticketExist(): Responses<T> {
            return fail(ResponseCodes.ticketExist, "ticket已存在")
        }

        fun <T> ticketNotExist(): Responses<T> {
            return fail(ResponseCodes.ticketNotExist, "ticket不存在")
        }

        /**
         * device code error response entity.
         */
        fun <T> deviceCodeError(): Responses<T> {
            return fail(ResponseCodes.deviceCodeError, "当前设备不存在")
        }

        fun <T> secretError(): Responses<T> {
            return fail(ResponseCodes.secretError, "secret错误")
        }

        fun <T> unicError(message: String = "unic服务器出现了异常"): Responses<T> {
            return fail(ResponseCodes.unicError, message)
        }

        fun <T> resourceNoAuth(): Responses<T> {
            return fail(ResponseCodes.resourceNoAuth, "你没有对该资源的访问权限")
        }

        fun <T> resourceReadonly(): Responses<T> {
            return fail(ResponseCodes.resourceReadOnly, "该资源是只读的")
        }

        fun <T> resourceNotExist(): Responses<T> {
            return fail(ResponseCodes.resourceNotExist, "当前资源不存在")
        }
    }
}