/**
 * author: h1542462994
 * response entity used in project.
 */

package org.tty.dailyset.dailyset_cloud.bean

/**
 * data class of response
 */
data class Responses<T>(
    val code: Int, // data
    val message: String, // message
    val data: T? // data
) {
    companion object {
        /**
         * default fail response entity.
         */
        fun <T> fail(code: Int = 1, message: String = "内部错误", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * default success response entity.
         */
        fun <T> ok(code: Int = 0, message: String = "请求成功", data: T? = null): Responses<T>
            = Responses(code, message, data)

        /**
         * default fail response entity in arg error.
         */
        fun <T> argError(code: Int = 2, message: String = "参数错误", data: T? = null): Responses<T>
            = Responses(code, message, data)
    }
}