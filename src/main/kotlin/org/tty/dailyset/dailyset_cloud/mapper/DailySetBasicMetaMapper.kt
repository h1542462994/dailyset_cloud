package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetBasicMeta

@Mapper
interface DailySetBasicMetaMapper {

    @Insert("insert into dailyset_basic_meta (meta_uid, name, icon) values (#{metaUid}, #{name}, #{icon})")
    fun addDailySetBasicMeta(dailySetBasicMeta: DailySetBasicMeta): Int

    @Select("select * from dailyset_basic_meta where meta_uid = #{metaUid}")
    fun findDailySetBasicMetaByMetaUid(metaUid: String): DailySetBasicMeta?
}