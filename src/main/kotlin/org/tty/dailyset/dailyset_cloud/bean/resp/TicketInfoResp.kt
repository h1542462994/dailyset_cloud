package org.tty.dailyset.dailyset_cloud.bean.resp

import org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus

class TicketInfoResp(
    val status: TicketStatus,
    val uid: String,
    val departmentName: String,
    val className: String,
    val name: String,
    val grade: Int
)