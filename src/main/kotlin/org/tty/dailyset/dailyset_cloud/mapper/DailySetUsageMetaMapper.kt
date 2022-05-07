package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetUsageMeta

@Mapper
interface DailySetUsageMetaMapper {
    @Select("select * from dailyset_usage_meta where user_uid = #{userUid}")
    fun findAllDailySetUsageMetaByUserUid(userUid: Int): List<DailySetUsageMeta>

    @Select("""
        <script>
            select * from dailyset_usage_meta where meta_uid in 
            <foreach collection="metaUids" item="metaUid" open="(" separator="," close=")">
                #{metaUid}
            </foreach>
        </script>
    """)
    fun findAllDailySetUsageMetaByMetaUidBatch(metaUids: List<String>): List<DailySetUsageMeta>
}