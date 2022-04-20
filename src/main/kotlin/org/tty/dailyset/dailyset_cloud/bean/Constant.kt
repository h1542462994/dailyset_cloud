/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.enums.PreferenceName

object Constant {
    const val BEARER = "Bearer "
    val PREFERENCE_DEFAULTS = mapOf(
         PreferenceName.NEXT_UID_GENERATE to "100001"
    )
}