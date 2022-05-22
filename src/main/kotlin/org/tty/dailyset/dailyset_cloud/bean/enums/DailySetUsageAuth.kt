package org.tty.dailyset.dailyset_cloud.bean.enums

enum class DailySetUsageAuth(
    val value: Int
) {
    Owner(0),
    Write(1),
    ReadOnly(2);

    companion object {
        fun of(value: Int): DailySetUsageAuth {
            return values().single { it.value == value }
        }
    }
}