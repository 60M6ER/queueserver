package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

    @Query(value = "select " +
            "       t.*, " +
            "       ts.priority " +
            "from" +
            "     ticket t left outer join ticket_service ts " +
            "         on t.ticket_service_id = ts.id " +
            "where t.status = 0 " +
            "order by ts.priority desc, t.date_create ",
    nativeQuery = true)
    List<Ticket> findAllToDistrib();

    @Query(value = "select * " +
            "from ticket t " +
            "where t.id in (select MAX(mt.ticket_id) " +
            "               from manager_ticket mt " +
            "               where manager_id = 2\n" +
            "               group by manager_id)\n" +
            "  and t.status = 1",
    nativeQuery = true)
    Ticket getTicketByManger(@Param("managerId") Long managerId);
}
