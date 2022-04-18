/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.tty.dailyset.dailyset_cloud.bean.entity.User

@Mapper
interface UserMapper {
    @Insert("insert into user (uid, nickname, email, password, portrait_id) values (#{uid}, #{nickname}, #{email}, #{password}, #{portraitId})")
    fun addUser(uid: Int, nickname: String, email: String, password: String, portraitId: String): Int

    @Select("select * from user where uid = #{uid}")
    fun findUserByUid(uid: Int): User?

}