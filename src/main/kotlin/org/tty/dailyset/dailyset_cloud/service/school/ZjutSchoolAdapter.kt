package org.tty.dailyset.dailyset_cloud.service.school

import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetBasicMeta
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetMetaLinks
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetUsageMeta
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetUsageAuth
import org.tty.dailyset.dailyset_cloud.grpc.stub.GrpcClientStubs
import org.tty.dailyset.dailyset_cloud.http.DailySetUnicApi
import org.tty.dailyset.dailyset_cloud.mapper.*
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
    private lateinit var dailySetUsageMetaMapper: DailySetUsageMetaMapper

    @Autowired
    private lateinit var dailySetMetaLinksMapper: DailySetMetaLinksMapper

    private val logger = LoggerFactory.getLogger(ZjutSchoolAdapter::class.java)

    override suspend fun getSchoolDailySets(userUid: String): Responses<List<DailySet>> {

        // 首先确认ticket状态
        val userTicketBind = userTicketBindMapper.findByUid(userUid)
            ?: return Responses.ticketNotExist()
        val ticketResult: TicketQueryResponse

        try {
            ticketResult = grpcClientStubs.getTicketClient().query {
                ticketId = userTicketBind.ticketId
            }
        } catch (e: StatusRuntimeException) {
            return Responses.unicError()
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
            dailySets.add(ensureDailySetGCreated(userUid = userUid, ticketResult.ticket.uid))
        }


        return Responses.ok(data = dailySets)
    }

    private fun getDailySet(): DailySet? {
        return dailySetMapper.findByUid(dailySetUid)
    }

    /**
     * 从远程服务器中获取信息
     */
    private suspend fun getRemoteDailySets(ticketId: String): List<DailySet> {
        val response = dailySetUnicApi.dailySetInfos(ticketId = ticketId)
        return try {
            if (response.code == ResponseCodes.success) {
                response.data!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            logger.error("on get unic info error:", e)
            emptyList()
        }
    }

    /**
     * 确定生成初始化的类似 **#school.zjut.course.2018x.g**的日程表，用于存放自动课表的其他信息（例如基础信息，主题等等）
     */
    private fun ensureDailySetGCreated(userUid: String, studentUid: String): DailySet {
        val dailySetGUid = "#school.zjut.course.${studentUid}.g"
        var dailySet = dailySetMapper.findByUid(dailySetGUid)

        if (dailySet == null) {
            // 默认名称是自动课表，图标为空.
            val dailySetNew = DailySet(dailySetGUid, DailySetType.Generated.value, 1, 1, 1)
            val dailySetBasicMeta = DailySetBasicMeta(uuid(), "自动课表", "")
            val dailySetMetaLinks = DailySetMetaLinks(dailySetGUid, DailySetMetaType.BasicMeta.value, dailySetBasicMeta.metaUid, 1, 0, 0, LocalDateTime.now())
            dailySetMapper.add(dailySetNew)
            dailySetBasicMetaMapper.add(dailySetBasicMeta)
            dailySetMetaLinksMapper.add(dailySetMetaLinks)
            dailySet = dailySetNew
        }

        // ensure add usage data
        var dailySetUsageMeta = dailySetUsageMetaMapper.findByDailySetUidAndUserUid(dailySetGUid, userUid)
        var metaVersion = dailySet.metaVersion
        if (dailySetUsageMeta == null) {
            metaVersion += 1
            dailySetUsageMeta = DailySetUsageMeta(
                metaUid = uuid(),
                dailySetUid = dailySetGUid,
                userUid = userUid,
                authType = DailySetUsageAuth.Owner.value
            )
            dailySetUsageMetaMapper.add(dailySetUsageMeta)
            dailySetMetaLinksMapper.add(
                DailySetMetaLinks(
                    dailySetUid = dailySetGUid,
                    metaType = DailySetMetaType.UsageMeta.value,
                    metaUid = dailySetUsageMeta.metaUid,
                    insertVersion = metaVersion,
                    updateVersion = 0,
                    removeVersion = 0,
                    lastTick = LocalDateTime.now()
                )
            )
        } else {
            if (dailySetUsageMeta.authType != DailySetUsageAuth.Owner.value) {
                metaVersion += 1

                dailySetUsageMetaMapper.update(
                    dailySetUsageMeta.copy(
                        authType = DailySetUsageAuth.Owner.value
                    )
                )
                val dailySetMetaLinks = dailySetMetaLinksMapper.findByDailySetUidAndMetaTypeAndMetaUid(
                    dailySetUid = dailySetGUid,
                    metaType = DailySetMetaType.UsageMeta.value,
                    metaUid = dailySetUsageMeta.metaUid
                )!!
                dailySetMetaLinksMapper.update(
                    dailySetMetaLinks.copy(
                        updateVersion = metaVersion,
                        lastTick = LocalDateTime.now()
                    )
                )
            }
        }

        if (metaVersion != dailySet.metaVersion) {
            dailySetMapper.update(
                dailySet.copy(metaVersion = metaVersion)
            )
        }

        return dailySet
    }
}