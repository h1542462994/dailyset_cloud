package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetSourceLinks

@Mapper
interface DailySetSourceLinkMapper {
    @Select("select * from dailyset_source_links where dailyset_uid = #{dailySetUid} and source_type = #{sourceType}")
    fun findAllDailySetSourceLinkByDailySetUidAndSourceType(dailySetUid: String, sourceType: Int): List<DailySetSourceLinks>

    @Select("""
        select * from dailyset_source_links where dailyset_uid = #{dailySetUid} and source_type = #{sourceType} and (
            insert_version > #{oldVersion} or update_version > #{oldVersion} or remove_version > #{oldVersion}
        )
    """)
    fun findAllDailySetSourceLinkByDailySetUidAndSourceTypeAndVersionsLargerThan(dailySetUid: String, sourceType: Int, oldVersion: Int): List<DailySetSourceLinks>
}