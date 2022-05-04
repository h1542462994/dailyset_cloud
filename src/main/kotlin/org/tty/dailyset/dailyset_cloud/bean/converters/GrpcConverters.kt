package org.tty.dailyset.dailyset_cloud.bean.converters

import org.tty.dailyset.dailyset_cloud.grpc.CurrentBindInfoResponse
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.CurrentBindInfoResponse
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.TicketBindInfo
import org.tty.dailyset.dailyset_unic.grpc.Ticket.TicketStatus
import org.tty.dailyset.dailyset_unic.grpc.Ticket.TicketStatus.*
import org.tty.dailyset.dailyset_unic.grpc.TicketQueryResponse
import org.tty.dailyset.dailyset_cloud.grpc.TicketBindInfo.TicketStatus as CTicketStatus


fun TicketQueryResponse.toCurrentBindInfoResponse(): CurrentBindInfoResponse {
    return CurrentBindInfoResponse {
        code = this@toCurrentBindInfoResponse.code
        message = this@toCurrentBindInfoResponse.message
        bindInfo = TicketBindInfo {
            status = this@toCurrentBindInfoResponse.ticket.status.toCTicketStatus()
            uid = this@toCurrentBindInfoResponse.studentInfo.uid
            departmentName = this@toCurrentBindInfoResponse.studentInfo.departmentName
            className = this@toCurrentBindInfoResponse.studentInfo.className
            name = this@toCurrentBindInfoResponse.studentInfo.name
            grade = this@toCurrentBindInfoResponse.studentInfo.grade
        }
    }
}

fun TicketStatus.toCTicketStatus(): CTicketStatus {
    return when (this) {
        Initialized -> CTicketStatus.Initialized
        Checked -> CTicketStatus.Checked
        Failure -> CTicketStatus.Failure
        PasswordFailure -> CTicketStatus.PasswordFailure
        UNRECOGNIZED -> CTicketStatus.UNRECOGNIZED
    }
}