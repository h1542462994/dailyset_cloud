package org.tty.dailyset.dailyset_cloud.bean.enums

enum class TicketStatus(val value: Int) {
    Initialized(0),
    Checked(1),
    Failure(2),
    PasswordFailure(3)
}