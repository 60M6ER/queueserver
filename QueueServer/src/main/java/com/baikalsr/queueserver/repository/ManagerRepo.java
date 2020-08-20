package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.TicketService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {
    Manager findByName(String name);
    Manager findByLoginAD(String name);

    @Query(value = "WITH mangerbyserviceandQueue AS " +
            "(select tsm.managers_id " +
            "from ticket_service_managers tsm left outer join manager m2 " +
            "    on  tsm.managers_id = m2.id " +
            "where tsm.ticket_services_id = :ticketServiceId " +
            "  and m2.queue_id = :queueId) " +

            "select * " +
            "from manager m " +
            "where m.id in (select ms.manager_id " +
            "from managers_status ms" +
            "               where ms.date in (select MAX(ms2.date) " +
            "                                 from managers_status ms2 " +
            "                                 where ms2.manager_id in (select mbsq.managers_id " +
            "                                                          from mangerbyserviceandQueue mbsq) " +
            "                                 group by ms2.manager_id) " +
            "                 and ms.manager_id in (select mbsq.managers_id " +
            "                                       from mangerbyserviceandQueue mbsq) " +
            "                 and ms.status = 'WORKING_TIME' " +
            "               order by ms.date) LIMIT 1 ",
            nativeQuery = true)
    Manager getManagerByTicketServiceToDistrib(@Param("ticketServiceId") Long ticketServiceId, @Param("queueId") Long queueId);
}
