package org.tty.dailyset.dailyset_cloud.http.resp

import kotlinx.serialization.json.JsonElement
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItemCollection
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

@kotlinx.serialization.Serializable
data class DailySetUpdateRawResult(
    val dailySet: DailySet,

    /**
     * updateItems
     * *JsonElement* is the dynamic raw json data.
     */
    val updateItems: List<DailySetUpdateItemCollection<JsonElement>>
)