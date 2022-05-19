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
    fun addUserTicketBind(userTicketBind: UserTicketBind): Int

    @Update("update user_ticket_bind set ticket_id = #{ticketId} where uid = #{uid}")
    fun updateUserTicketBindByUid(userTicketBind: UserTicketBind): Int

    @Select("select * from user_ticket_bind where uid = #{uid}")
    fun findUserTicketBindByUid(uid: Int): UserTicketBind?

    @Delete("delete from user_ticket_bind where uid = #{uid}")
    fun removeUserTicketBindByUid(uid: Int): Int

    @Select("""
        <script>
            select * from user_ticket_bind where ticket_id in 
            <foreach collection="ticketIds" item="ticketId" open="(" separator="," close=")">
                #{ticketId}
            </foreach>
        </script>
    """)
    fun findAllUserTicketBindByTicketIdBatch(ticketIds: List<String>): List<UserTicketBind>
}