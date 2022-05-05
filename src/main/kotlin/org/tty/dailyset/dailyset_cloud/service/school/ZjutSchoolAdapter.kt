package org.tty.dailyset.dailyset_cloud.service.school

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.component.GrpcClientStubs
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper

@Component
class ZjutSchoolAdapter: SchoolAdapter {
    override val dailySetUid: String = "#school.zjut.global"
    override val name: String = "浙江工业大学"

    @Autowired
    private lateinit var grpcClientStubs: GrpcClientStubs

    @Autowired
    private lateinit var userTicketBindMapper: UserTicketBindMapper

    @Autowired
    private lateinit var dailySetMapper: DailySetMapper

    override suspend fun getSchoolDailyset(userUid: Int): Responses<DailySet> {

        // 首先确认ticket状态
        val userTicketBind = userTicketBindMapper.findUserTicketBindByUid(userUid)
            ?: return Responses.fail(code = ResponseCodes.ticketNotExist, message = "未绑定ticket")

        val ticketResult = grpcClientStubs.getTicketClient().query {
            ticketId = userTicketBind.ticketId
        }

        // 没有成功获取到信息
        if (ticketResult.code != ResponseCodes.success) {
            return Responses.fail(code = ticketResult.code, message = ticketResult.message)
        }

        val dailySet = getDailySet()
        return if (dailySet == null) {
            Responses.fail(message = "数据库中无该数据")
        } else {
            Responses.ok(data = dailySet)
        }
    }

    private fun getDailySet(): DailySet? {
        return dailySetMapper.findDailySetByUid(dailySetUid)
    }
}