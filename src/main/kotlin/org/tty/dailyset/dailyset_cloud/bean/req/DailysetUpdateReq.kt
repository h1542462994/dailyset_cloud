package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.util.anyIntEmpty
import org.tty.dailyset.dailyset_cloud.util.anyTextEmpty

class DailysetUpdateReq(
    val uid: String?,
    val type: Int?,
    val sourceVersion: Int?,
    val matteVersion: Int?,
    val metaVersion: Int?
) {
    fun verify(): Boolean {
        return !anyIntEmpty(type, sourceVersion, matteVersion, metaVersion) && !anyTextEmpty(uid)
    }
}