package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetCell

@Mapper
interface DailySetCellMapper {
    @Select("""
        <script>
            select * from dailyset_cell where source_uid in 
            <foreach collection="sourceUids" item="sourceUid" open="(" separator="," close=")">
                #{sourceUid}
            </foreach>
        </script>
    """)
    fun findAllBySourceUid(sourceUids: List<String>): List<DailySetCell>
}