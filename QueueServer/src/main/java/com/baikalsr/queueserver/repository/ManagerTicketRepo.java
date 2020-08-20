package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.ManagerTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ManagerTicketRepo extends JpaRepository<ManagerTicket, Long> {

    @Query(value = "select * from manager_ticket m where m.ticket_id = :ticketId order by m.date desc limit 1",
    nativeQuery = true)
    ManagerTicket getLastByTicketId(@Param("ticketId") Long ticketId);
}
