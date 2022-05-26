package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetUsageMeta

@Mapper
interface DailySetUsageMetaMapper {
    @Select("select * from dailyset_usage_meta where user_uid = #{userUid}")
    fun findAllByUserUid(userUid: String): List<DailySetUsageMeta>

    @Select("select * from dailyset_usage_meta where dailyset_uid = #{dailySetUid} and user_uid = #{userUid} limit 1")
    fun findByDailySetUidAndUserUid(dailySetUid: String, userUid: String): DailySetUsageMeta?

    @Select("""
        <script>
            select * from dailyset_usage_meta where meta_uid in 
            <foreach collection="metaUids" item="metaUid" open="(" separator="," close=")">
                #{metaUid}
            </foreach>
        </script>
    """)
    fun findAllByMetaUid(metaUids: List<String>): List<DailySetUsageMeta>

    @Insert("insert into dailyset_usage_meta (meta_uid, dailyset_uid, user_uid, auth_type) values (#{metaUid}, #{dailySetUid}, #{userUid}, #{authType})")
    fun add(dailySetUsageMeta: DailySetUsageMeta): Int

    @Update("""
        update dailyset_usage_meta set dailyset_uid = #{dailySetUid}, user_uid = #{userUid}, auth_type = #{authType}
        where meta_uid = #{metaUid}
    """)
    fun update(dailySetUsageMeta: DailySetUsageMeta): Int
}