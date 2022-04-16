package org.tty.dailyset.dailyset_cloud.util

import okhttp3.internal.and
import java.io.File

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

fun anyEmpty(vararg texts: String?): Boolean {
    return texts.any { it.isNullOrEmpty() }
}