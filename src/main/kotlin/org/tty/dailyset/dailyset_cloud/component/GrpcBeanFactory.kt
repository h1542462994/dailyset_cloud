package org.tty.dailyset.dailyset_cloud.component

import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.grpc.TicketBindInfo
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.TicketBindInfo

@Component
class GrpcBeanFactory {
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