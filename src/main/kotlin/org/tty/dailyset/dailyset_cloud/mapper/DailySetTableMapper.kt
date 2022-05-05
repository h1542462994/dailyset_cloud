package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetTable

@Mapper
interface DailySetTableMapper {

    @Select("""
        <script>
            select * from dailyset_table where source_uid in 
            <foreach collection = "sourceUids" item = "sourceUid" open = "(" separator = "," close = ")">
                #{sourceUid}
            </foreach>
        </script>
    """)
    fun findAllDailyTableBySourceUidBatch(sourceUids: List<String>): List<DailySetTable>
}