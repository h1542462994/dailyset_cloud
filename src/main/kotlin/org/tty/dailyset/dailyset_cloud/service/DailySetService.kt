package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.service.school.SchoolAdapter

@Component
class DailySetService {

    @Autowired
    private lateinit var schoolAdapter: SchoolAdapter

    /**
     * 获取所有Dailyset的数据.
     */
    suspend fun getDailysetInfos(userUid: Int): List<DailySet> {
        // 获取自动课表
        val autoDailySet = schoolAdapter.getSchoolDailyset(userUid)

        TODO()
    }

    /**
     * 获取某个Dailyset的更新数据
     */
    fun getUpdates(dailySetUid: String, oldVersion: Int, targetVersion: Int) {

    }

}