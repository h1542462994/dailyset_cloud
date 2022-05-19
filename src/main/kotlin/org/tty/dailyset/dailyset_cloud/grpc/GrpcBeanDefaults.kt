package org.tty.dailyset.dailyset_cloud.grpc

import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.TicketBindInfo

object GrpcBeanDefaults {
    fun emptyBindInfo(): TicketBindInfo {
        return TicketBindInfo {
            status = TicketBindInfo.TicketStatus.Initialized
            uid = ""
            departmentName = ""
            className = ""
            name = ""
            grade = 0
        }
    }

}