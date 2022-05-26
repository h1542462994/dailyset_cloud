/**
 * create at 2022/4/16
 * author h1542462994
 *
 * mapper class UserActivityMapper
 */

package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.tty.dailyset.dailyset_cloud.bean.entity.UserActivity

/**
 * mapper class -> entity [UserActivity]
 * @see UserActivity
 */
@Mapper
interface UserActivityMapper {

    @Insert("insert into user_activity(uid, device_code, device_name, platform_code, state, last_active) values (#{uid}, #{deviceCode}, #{deviceName}, #{platformCode}, #{state}, #{lastActive})")
    fun add(userActivity: UserActivity): Int

    @Select("select * from user_activity where uid = #{uid} and device_code = #{deviceCode}")
    fun findByUidAndDeviceCode(uid: String, deviceCode: String): UserActivity?

    @Update("update user_activity set device_name = #{deviceName}, platform_code = #{platformCode}, state = #{state}, last_active = #{lastActive} where uid = #{uid} and device_code = #{deviceCode}")
    fun updateByUidAndDeviceCode(userActivity: UserActivity): Int

}