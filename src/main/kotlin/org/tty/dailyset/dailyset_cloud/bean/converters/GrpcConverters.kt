package org.tty.dailyset.dailyset_cloud.bean.converters

import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.resp.TicketInfoResp
import org.tty.dailyset.dailyset_cloud.grpc.CommonProtoBuilders.SimpleResponse
import org.tty.dailyset.dailyset_cloud.grpc.CurrentBindInfoResponse
import org.tty.dailyset.dailyset_cloud.grpc.GrpcBeanDefaults
import org.tty.dailyset.dailyset_cloud.grpc.SimpleResponse
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

fun TicketQueryResponse.toRTicketInfoResp(): Responses<TicketInfoResp> {
    return Responses(
        code = this.code,
        message = this.message,
        data = TicketInfoResp(
            status = this.ticket.status.toTicketStatus(),
            uid = this.studentInfo.uid,
            departmentName = this.studentInfo.departmentName,
            className = this.studentInfo.className,
            name = this.studentInfo.name,
            grade = this.studentInfo.grade
        )
    )
}

fun TicketStatus.toTicketStatus(): org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus {
    return when (this) {
        Initialized -> org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.Initialized
        Checked -> org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.Checked
        Failure -> org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.Failure
        PasswordFailure -> org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.PasswordFailure
        UNRECOGNIZED -> error("not recognized.")
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

fun org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.toCTicketStatus(): CTicketStatus {
    return when (this) {
        org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.Initialized -> CTicketStatus.Initialized
        org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.Checked -> CTicketStatus.Checked
        org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.Failure -> CTicketStatus.Failure
        org.tty.dailyset.dailyset_cloud.bean.enums.TicketStatus.PasswordFailure -> CTicketStatus.PasswordFailure
    }
}

fun Responses<Unit>.toSimpleResponse(): SimpleResponse {
    val v = this@toSimpleResponse
    return SimpleResponse {
        code = v.code
        message = v.message
    }
}


fun Responses<TicketInfoResp>.toCurrentTicketInfoResp(): CurrentBindInfoResponse {
    val v = this@toCurrentTicketInfoResp
    return CurrentBindInfoResponse {
        code = v.code
        message = v.message
        bindInfo = if (v.data == null) {
            GrpcBeanDefaults.emptyBindInfo()
        } else {
            TicketBindInfo {
                status = v.data.status.toCTicketStatus()
                uid = v.data.uid
                departmentName = v.data.departmentName
                className = v.data.className
                name = v.data.name
                grade = v.data.grade
            }
        }
    }
}