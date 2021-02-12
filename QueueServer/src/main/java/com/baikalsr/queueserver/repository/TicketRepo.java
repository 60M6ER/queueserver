package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Ticket;
import com.baikalsr.queueserver.entity.TicketStatus;
import com.baikalsr.queueserver.jsonView.tablo.TicketsListForTabloPOJO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Repository
public interface TicketRepo extends JpaRepository<Ticket, UUID> {

    @Query(value = "select \n" +
            "t.*, \n" +
            "ts.priority \n" +
            "from ticket t join ticket_service ts \n" +
            "on (t.ticket_service_id = ts.id\n" +
            "   and t.status = 0) \n" +
            "order by ts.priority desc, t.date_create limit 100",
    nativeQuery = true)
    List<Ticket> findAllToDistrib();

    @Query(value = "select t.*, ts.priority \n" +
            "from ticket t join ticket_service ts \n" +
            "on (t.ticket_service_id = ts.id \n" +
            "\tand t.id = :id) \n" +
            "\t\n" +
            "union all \n" +
            "select \n" +
            "t.*, \n" +
            "ts.priority \n" +
            "from ticket t join ticket_service ts \n" +
            "on (t.ticket_service_id = ts.id \n" +
            "\tand t.status = 0\n" +
            "   and t.queue_id = :queueId) \n" +
            "order by priority desc, date_create",
            nativeQuery = true)
    List<Ticket> findAllInQueueByQueue(@Param("queueId") Long queueId, @Param("id") UUID ticketId);

    @Query(value =
            "select * " +
            "from ticket t join manager_ticket mt " +
            "on (t.id = mt.ticket_id " +
            "   and mt.manager_id = :managerId " +
            "   and (t.status = 1 or t.status = 2)) " +
                    "order by mt.date desc " +
            "limit 1",
    nativeQuery = true)
    Ticket getServicingTicketByManger(@Param("managerId") Long managerId);

    Ticket getTicketByPrintJobID(UUID uuid);

    @Query(value = "select count (*) " +
            "from ticket t " +
            "where queue_id = :queueid " +
            "and t.status = 0",
            nativeQuery = true)
    Integer getCountClientsInQueue(@Param("queueid") Long queue);

    @Query(value = "select * " +
            "from ticket t join manager_ticket mt " +
            "on (t.id = mt.ticket_id " +
            "   and mt.manager_id = :managerId " +
            "   and t.status = 3) ",
            nativeQuery = true)
    List<Ticket> getPausedTicketsByManger(@Param("managerId") Long managerId);

    @Query(value = "select * \n" +
            "from ticket t \n" +
            "where\n" +
            "\t t.queue_id = :queue_id\n" +
            "\tand t.date_create > :start_date\n" +
            "\tand t.date_create <= :end_date\n" +
            "\tand t.name like concat('%', :number)",
            nativeQuery = true)
    List<Ticket> getAllByNumberAndQueueAndStartDateAndEndDate(@Param("number") String number,
                                                              @Param("queue_id") Long queueId,
                                                              @Param("start_date") Date startDate,
                                                              @Param("end_date") Date endDate);

    @Query(value = "select * \n" +
            "from ticket t \n" +
            "order by t.date_create",
            nativeQuery = true)
    List<Ticket> findAllSorted();
}
