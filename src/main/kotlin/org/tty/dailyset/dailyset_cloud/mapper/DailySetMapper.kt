package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

@Mapper
interface DailySetMapper {
    @Select("select * from dailyset where uid = #{uid}")
    fun findDailySetByUid(uid: String): DailySet?

    @Select("""
        <script>
            select * from dailyset where uid in 
            <foreach collection = "uids" item = "uid" open = "(" separator = "," close = ")">
                #{uid}
            </foreach>
        </script>
    """)
    fun findDailySetByUidBatch(uids: List<String>): List<DailySet>
}