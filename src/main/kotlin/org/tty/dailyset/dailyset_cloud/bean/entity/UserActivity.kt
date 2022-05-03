/**
 * create at 2022/4/16
 * author h1542462994
 *
 * entity class UserActivity
 */

package org.tty.dailyset.dailyset_cloud.bean.entity

import java.time.LocalDateTime

/**
 * entity class -> table user_activity
 */
data class UserActivity(
    val uid: Int,
    val deviceCode: String,
    val deviceName: String,
    val platformCode: Int,
    val state: Int,
    val lastActive: LocalDateTime
)