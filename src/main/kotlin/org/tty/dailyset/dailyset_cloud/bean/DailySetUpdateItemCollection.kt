package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDataType

data class DailySetUpdateItemCollection<T: Any>(
    /**
     * **type** 数据类型 **source | matte | meta**
     * @see DailySetDataType
     */
    val type: Int,

    /**
     * **subType** 子数据类型 **?**
     */
    val subType: Int,


    val updates: List<DailySetUpdateItem<T>>)