package org.tty.dailyset.dailyset_cloud.bean.converters

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItemCollection
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import org.tty.dailyset.dailyset_cloud.bean.entity.*
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDataType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.http.resp.DailySetUpdateRawResult

fun DailySetUpdateRawResult?.toDailysetUpdateResultNoTrans(): DailySetUpdateResult? {
    if (this == null) {
        return null
    }

    return DailySetUpdateResult(
        dailySet = this.dailySet,
        updateItems = this.updateItems
    )
}

fun DailySetUpdateRawResult?.toDailySetUpdateResultTrans(): DailySetUpdateResult? {
    if (this == null) {
        return null
    }
    return DailySetUpdateResult(
        dailySet = this.dailySet,
        updateItems = this.updateItems.map {
            DailySetUpdateItemCollection(
                type = it.type,
                subType = it.subType,
                updates = it.updates.map { element ->
                    DailySetUpdateItem(
                        insertVersion = element.insertVersion,
                        updateVersion = element.updateVersion,
                        removeVersion = element.removeVersion,
                        lastTick = element.lastTick,
                        data = element.data.castToReal(it.type, it.subType)
                    )
                }
            )
        }
    )
}

private fun JsonElement?.castToReal(type: Int, subType: Int): Any? {
    if (this == null) {
        return null
    }
    return when(type) {
        DailySetDataType.Source.value -> {
            when(subType) {
                DailySetSourceType.Table.value -> Json.decodeFromJsonElement<DailySetTable>(this)
                DailySetSourceType.Row.value -> Json.decodeFromJsonElement<DailySetRow>(this)
                DailySetSourceType.Cell.value -> Json.decodeFromJsonElement<DailySetCell>(this)
                DailySetSourceType.Duration.value -> Json.decodeFromJsonElement<DailySetDuration>(this)
                DailySetSourceType.Course.value -> Json.decodeFromJsonElement<DailySetCourse>(this)
                else -> throw IllegalStateException("type not supported.")
            }
        }
        DailySetDataType.Meta.value -> {
            when(subType) {
                DailySetMetaType.BasicMeta.value -> Json.decodeFromJsonElement<DailySetBasicMeta>(this)
                DailySetMetaType.UsageMeta.value -> Json.decodeFromJsonElement<DailySetUsageMeta>(this)
                DailySetMetaType.SchoolMeta.value -> Json.decodeFromJsonElement<DailySetSchoolInfoMeta>(this)
                DailySetMetaType.StudentInfoMeta.value -> Json.decodeFromJsonElement<DailySetStudentInfoMeta>(this)
                else -> throw IllegalStateException("type not supported.")
            }
        }
        else -> throw IllegalStateException("type not supported.")
    }
}