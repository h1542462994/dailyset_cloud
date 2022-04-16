package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper {
    @Insert("insert into user (uid, nickname, email, password, portrait_id) values (#{uid}, #{nickname}, #{email}, #{password}, #{portraitId})")
    fun addUser(uid: Int, nickname: String, email: String, password: String, portraitId: String): Int
}