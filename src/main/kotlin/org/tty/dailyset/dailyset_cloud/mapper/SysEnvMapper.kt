/**
 * create at 2022/4/16
 * author h1542462994
 *
 * entity mapper class SysEnvMapper
 */

package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.tty.dailyset.dailyset_cloud.bean.entity.SysEnv

/**
 * mapper class -> entity [SysEnv]
 * @see SysEnv
 */
@Deprecated("use PreferenceMapper instead.")
@Mapper
interface SysEnvMapper {
    @Select("select * from sys_env limit 1")
    fun find(): SysEnv

    @Update("update sys_env set next_uid_generate = #{uid}")
    fun updateNextUidGenerate(uid: Int): Int


}