package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetBasicMeta

@Mapper
interface DailySetBasicMetaMapper {

    @Insert("insert into dailyset_basic_meta (meta_uid, name, icon) values (#{metaUid}, #{name}, #{icon})")
    fun add(dailySetBasicMeta: DailySetBasicMeta): Int

    @Select("select * from dailyset_basic_meta where meta_uid = #{metaUid}")
    fun findByMetaUid(metaUid: String): DailySetBasicMeta?

    fun update(dailySetBasicMeta: DailySetBasicMeta): Int
}