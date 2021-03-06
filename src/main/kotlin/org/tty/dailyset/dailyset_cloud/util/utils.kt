/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.util

import okhttp3.internal.and
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

fun File.child(name: String): File = File(this, name)

/**
 * slow functions
 */
fun byte2Hex(bytes: ByteArray): String {
    val stringBuilder = StringBuilder()
    var temp: String
    for (byte in bytes) {
        temp = Integer.toHexString(byte and 0xFF)
        if (temp.length == 1) {
            stringBuilder.append("0")
        }
        stringBuilder.append(temp)
    }
    return stringBuilder.toString()
}

fun anyTextEmpty(vararg texts: String?): Boolean {
    return texts.any { it.isNullOrEmpty() }
}

fun anyIntEmpty(vararg ints: Int?): Boolean {
    return ints.any { it == null }
}

fun uuid(): String {
    return UUID.randomUUID().toString()
}

fun getToken(auth: String): String?{
    return if (auth.startsWith("Bearer ")) {
        auth.substring("Bearer ".length)
    } else {
        null
    }
}

fun <T> MutableList<T>.addNotNull(value: T?) {
    if (value != null) {
        add(value)
    }
}

fun coerceLocalDateTime(instant: LocalDateTime, nowLocal: LocalDateTime): LocalDateTime {
    val now = LocalDateTime.now()
    val diff = Duration.between(now, nowLocal)
    return instant.plus(diff)
}