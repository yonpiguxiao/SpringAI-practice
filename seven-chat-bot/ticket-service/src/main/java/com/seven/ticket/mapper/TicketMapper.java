package com.seven.ticket.mapper;

import com.seven.ticket.entity.TicketInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TicketMapper {
    @Insert("""
        insert into ticket_info (ticket_id, title, `description`, related_chat_id) values
        (#{ticketId}, #{title}, #{description}, #{relatedChatId})
    """)
    Integer insertTicket(TicketInfo ticketInfo);

    @Select("select * from ticket_info where ticket_id = #{ticketId}")
    TicketInfo selectById(String ticketId);
}
