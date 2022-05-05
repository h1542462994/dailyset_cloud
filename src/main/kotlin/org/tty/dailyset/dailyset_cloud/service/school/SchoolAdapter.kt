package org.tty.dailyset.dailyset_cloud.service.school

import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

interface SchoolAdapter {
    val dailySetUid: String
    val name: String

    suspend fun getSchoolDailyset(userUid: Int): Responses<DailySet>
}