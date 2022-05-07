package org.tty.dailyset.dailyset_cloud.service.school

import io.grpc.StatusRuntimeException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetBasicMeta
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetMetaLinks
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetType
import org.tty.dailyset.dailyset_cloud.grpc.stub.GrpcClientStubs
import org.tty.dailyset.dailyset_cloud.http.req.DailySetInfosReqUnic
import org.tty.dailyset.dailyset_cloud.http.DailySetUnicApi
import org.tty.dailyset.dailyset_cloud.mapper.DailySetBasicMetaMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMetaLinksMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper
import org.tty.dailyset.dailyset_cloud.util.addNotNull
import org.tty.dailyset.dailyset_cloud.util.uuid
import org.tty.dailyset.dailyset_unic.grpc.TicketQueryResponse
import java.time.LocalDateTime

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

    @Autowired
    private lateinit var dailySetUnicApi: DailySetUnicApi

    @Autowired
    private lateinit var dailySetBasicMetaMapper: DailySetBasicMetaMapper

    @Autowired
    private lateinit var dailySetMetaLinksMapper: DailySetMetaLinksMapper

    override suspend fun getSchoolDailySets(userUid: Int): Responses<List<DailySet>> {

        // 首先确认ticket状态
        val userTicketBind = userTicketBindMapper.findUserTicketBindByUid(userUid)
            ?: return Responses.fail(code = ResponseCodes.ticketNotExist, message = "未绑定ticket")
        val ticketResult: TicketQueryResponse

        try {
            ticketResult = grpcClientStubs.getTicketClient().query {
                ticketId = userTicketBind.ticketId
            }
        } catch (e: StatusRuntimeException) {
            return Responses.fail(code = ResponseCodes.unicError, message = "上游服务器出现了错误")
        }

        // 没有成功获取到信息
        if (ticketResult.code != ResponseCodes.success) {
            return Responses.fail(code = ticketResult.code, message = ticketResult.message)
        }

        val dailySets = mutableListOf<DailySet>()
        val globalDailySet = getDailySet()
        dailySets.addNotNull(globalDailySet)
        val resultList = getRemoteDailySets(userTicketBind.ticketId)
        dailySets.addAll(resultList)
        if (resultList.any {
                "^#school.zjut.course.[\\dA-Za-z_-]+$".toRegex().matches(it.uid)
            }) {
            dailySets.add(ensureDailySetGCreated(ticketResult.ticket.uid))
        }


        return Responses.ok(data = dailySets)
    }

    private fun getDailySet(): DailySet? {
        return dailySetMapper.findDailySetByUid(dailySetUid)
    }

    /**
     * 从远程服务器中获取信息
     */
    private suspend fun getRemoteDailySets(ticketId: String): List<DailySet> {
        val response = dailySetUnicApi.dailySetInfos(DailySetInfosReqUnic(ticketId = ticketId))
        return if (response.code == ResponseCodes.success) {
            response.data!!
        } else {
            emptyList()
        }
    }

    /**
     * 确定生成初始化的类似 **#school.zjut.course.2018x.g**的日程表，用于存放自动课表的其他信息（例如基础信息，主题等等）
     */
    private fun ensureDailySetGCreated(studentUid: String): DailySet {
        val dailySetGUid = "#school.zjut.course.${studentUid}.g"
        val dailySet = dailySetMapper.findDailySetByUid(dailySetGUid)
        return if (dailySet != null) {
            dailySet
        } else {
            // 默认名称是自动课表，图标为空.
            val dailySetNew = DailySet(dailySetGUid, DailySetType.Generated.value, 1, 1, 1)
            val dailySetBasicMeta = DailySetBasicMeta(uuid(), "自动课表", "")
            val dailySetMetaLinks = DailySetMetaLinks(dailySetGUid, DailySetMetaType.BasicMeta.value, dailySetBasicMeta.metaUid, 1, 0, 0, LocalDateTime.now())
            dailySetMapper.addDailySet(dailySetNew)
            dailySetBasicMetaMapper.addDailySetBasicMeta(dailySetBasicMeta)
            dailySetMetaLinksMapper.addDailySetMetaLinks(dailySetMetaLinks)
            dailySetNew
        }
    }
}