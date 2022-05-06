package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetMetaLinks

@Mapper
interface DailySetMetaLinksMapper {
    @Insert("""
        insert into dailyset_meta_links (dailyset_uid, meta_type, meta_uid, insert_version, update_version, remove_version, last_tick)
        values (#{dailySetUid}, #{metaType}, #{metaUid}, #{insertVersion}, #{updateVersion}, #{removeVersion}, #{lastTick})
    """)
    fun addDailySetMetaLinks(dailySetMetaLinks: DailySetMetaLinks): Int

    @Select("""
        select * from dailyset_meta_links where dailyset_uid = #{dailySetUid} and meta_type = #{metaType} and (
            insert_version > #{oldVersion} or update_version > #{oldVersion} or remove_version > #{oldVersion}
        )
    """)
    fun findAllDailySetMetaLinkByDailySetUidAndSourceTypeAndVersionsLargerThan(dailySetUid: String, metaType: Int, oldVersion: Int): List<DailySetMetaLinks>
}