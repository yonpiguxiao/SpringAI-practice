package com.seven.ticket;

import com.seven.ticket.entity.TicketInfo;
import com.seven.ticket.mapper.TicketMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class TicketTest {
    @Autowired
    private TicketMapper ticketMapper;

    @Test
    void test() {
        TicketInfo ticketInfo = parseTicketInfo("test", "test", "00000");
        ticketMapper.insertTicket(ticketInfo);
    }
    @Test
    void test1() {
        TicketInfo ticketInfo = ticketMapper.selectById("CSQ-1772971405679-3728");
        System.out.println(ticketInfo);
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
}
