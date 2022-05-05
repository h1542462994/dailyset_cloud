package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetRow

@Mapper
interface DailySetRowMapper {

    @Select("""
        <script>
            select * from dailyset_row where source_uid in 
            <foreach collection = "sourceUids" item = "sourceUid" open = "(" separator = "," close = ")">
                #{sourceUid}
            </foreach>
        </script>
    """)
    fun findAllDailySetRowBySourceUidBatch(sourceUids: List<String>): List<DailySetRow>

}