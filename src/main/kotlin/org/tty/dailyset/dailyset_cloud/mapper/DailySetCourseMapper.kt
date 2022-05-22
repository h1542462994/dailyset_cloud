package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetCourse

@Mapper
interface DailySetCourseMapper {
    @Select("""
        <script>
            select * from dailyset_course where source_uid in 
            <foreach collection="sourceUids" item="sourceUid" open="(" separator="," close=")">
                #{sourceUid}
            </foreach>
        </script>
    """)
    fun findAllBySourceUid(sourceUids: List<String>): List<DailySetCourse>
}