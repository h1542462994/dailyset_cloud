package org.tty.dailyset.dailyset_cloud.mapper

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.tty.dailyset.dailyset_cloud.bean.entity.UserTicketBind

@Mapper
interface UserTicketBindMapper {
    @Insert("insert into user_ticket_bind (uid, ticket_id) values (#{uid}, #{ticketId})")
    fun add(userTicketBind: UserTicketBind): Int

    @Update("update user_ticket_bind set ticket_id = #{ticketId} where uid = #{uid}")
    fun update(userTicketBind: UserTicketBind): Int

    @Select("select * from user_ticket_bind where uid = #{uid}")
    fun findByUid(uid: String): UserTicketBind?

    @Delete("delete from user_ticket_bind where uid = #{uid}")
    fun remove(uid: String): Int

    @Select("""
        <script>
            select * from user_ticket_bind where ticket_id in 
            <foreach collection="ticketIds" item="ticketId" open="(" separator="," close=")">
                #{ticketId}
            </foreach>
        </script>
    """)
    fun findAllByTicketId(ticketIds: List<String>): List<UserTicketBind>
}