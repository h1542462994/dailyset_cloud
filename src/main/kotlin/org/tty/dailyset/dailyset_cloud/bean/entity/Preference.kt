package org.tty.dailyset.dailyset_cloud.bean.entity

/**
 * entity class -> preference
 */
data class Preference(
    val preferenceName: String,
    val useDefault: Boolean,
    val value: String
)
