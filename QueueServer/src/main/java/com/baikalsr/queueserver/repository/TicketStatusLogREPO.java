package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.TicketStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Repository
public interface TicketStatusLogREPO extends JpaRepository<TicketStatusLog, UUID> {

    @Query(value = "select * " +
            "from ticket_status_log tsl " +
            "where tsl.ticket_id = :ticket_id " +
            "order by tsl.date",
            nativeQuery = true)
    List<TicketStatusLog> getAllByTicketOrOrderByDate(@Param("ticket_id") UUID ticket);
}
