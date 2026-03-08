package com.seven.ticket.service;


import com.seven.ticket.entity.TicketInfo;
import com.seven.ticket.mapper.TicketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Service
public class TicketService {
    @Autowired
    private TicketMapper ticketMapper;

    @Tool(description = "根据工单标题, 描述和关联的会话Id, 创建工单")
    public String createTicket(@ToolParam(description = "工单标题, 不能为空") String title,
                               @ToolParam(description = "工单描述, 不能为空") String description,
                               @ToolParam(description = "关联的会话Id, 不能为空") String relatedId){
        log.info("创建工单, title:{}", title);
        if(!StringUtils.hasText(title)) {
            return "创建工单失败,缺少必填参数title";
        }
        if(!StringUtils.hasText(description)) {
            return "创建工单失败,缺少必填参数description";
        }
        if(!StringUtils.hasText(relatedId)) {
            return "创建工单失败,缺少必填参数relatedId";
        }
        TicketInfo ticketInfo = parseTicketInfo(title, description, relatedId);
        try {
            ticketMapper.insertTicket(ticketInfo);
            return String.format("创建工单成功,工单ID:%s", ticketInfo.getTicketId());
        } catch (Exception e) {
            return "创建工单失败, e:" + e.getMessage();
        }
    }

    private TicketInfo parseTicketInfo(String title, String description, String relatedId) {
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setTicketId(generateTicketId());
        ticketInfo.setTitle(title);
        ticketInfo.setDescription(description);
        ticketInfo.setRelatedChatId(relatedId);
        return ticketInfo;
    }

    private String generateTicketId() {
        return String.format("CSQ-%d-%04d", System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(1000,10000));
    }

    @Tool(description = "根据工单ID查询工单信息")
    public TicketInfo queryTicketInfo(@ToolParam(description = "工单Id, 不能为空") String ticketId) {
        log.info("查询工单, 工单Id:{}", ticketId);
        if(!StringUtils.hasText(ticketId)) {
            return null;
        }
        TicketInfo ticketInfo = ticketMapper.selectById(ticketId);
        return ticketInfo;
    }
}
