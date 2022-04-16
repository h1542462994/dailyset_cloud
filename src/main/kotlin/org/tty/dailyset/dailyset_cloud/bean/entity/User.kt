/**
 * create at 2022/4/15
 * author h1542462994
 *
 * entity class User.
 */

package org.tty.dailyset.dailyset_cloud.bean.entity

/**
 * entity class -> table *user*
 */
data class User(
    val uid: Int,
    val nickname: String,
    val password: String,
    val email: String,
    val portraitId: String
)