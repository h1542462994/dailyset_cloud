package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.tty.dailyset.dailyset_cloud.bean.entity.SysEnv

@Mapper
interface SysEnvMapper {
    @Select("select * from sys_env limit 1")
    fun get(): SysEnv

    @Update("update sys_env set next_uid_generate = #{uid}")
    fun setUid(uid: Int): Int
}