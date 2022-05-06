package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
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

    @Insert("""
        insert into dailyset (uid, type, source_version, matte_version, meta_version) 
        values (#{uid}, #{type}, #{sourceVersion}, #{matteVersion}, #{metaVersion})
    """)
    fun addDailySet(dailySet: DailySet): Int

    @Update("""
        update dailyset set type = #{type}, source_version = #{sourceVersion}, matte_version = #{matteVersion}, meta_version = #{metaVersion}
        where uid = #{uid}
    """)
    fun updateDailySet(dailySet: DailySet): Int
}