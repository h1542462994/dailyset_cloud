package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetMetaLinks

@Mapper
interface DailySetMetaLinksMapper {
    @Insert("""
        insert into dailyset_meta_links (dailyset_uid, meta_type, meta_uid, insert_version, update_version, remove_version, last_tick)
        values (#{dailySetUid}, #{metaType}, #{metaUid}, #{insertVersion}, #{updateVersion}, #{removeVersion}, #{lastTick})
    """)
    fun add(dailySetMetaLinks: DailySetMetaLinks): Int

    @Select("select * from dailyset_meta_links where dailyset_uid = #{dailySetUid} and meta_type = #{metaType} and meta_uid = #{metaUid}")
    fun findByDailySetUidAndMetaTypeAndMetaUid(dailySetUid: String, metaType: Int, metaUid: String): DailySetMetaLinks?

    @Update("""
        update dailyset_meta_links set
            insert_version = #{insertVersion}, update_version = #{updateVersion}, remove_version = #{removeVersion}, last_tick = #{lastTick}
        where dailyset_uid = #{dailySetUid} and meta_type = #{metaType} and meta_uid = #{metaUid}
    """)
    fun update(dailySetMetaLinks: DailySetMetaLinks): Int

    @Select("""
        select * from dailyset_meta_links where dailyset_uid = #{dailySetUid} and meta_type = #{metaType} and (
            insert_version > #{oldVersion} or update_version > #{oldVersion} or remove_version > #{oldVersion}
        )
    """)
    fun findAllByDailySetUidAndMetaTypeAndVersionsLargerThan(dailySetUid: String, metaType: Int, oldVersion: Int): List<DailySetMetaLinks>
}