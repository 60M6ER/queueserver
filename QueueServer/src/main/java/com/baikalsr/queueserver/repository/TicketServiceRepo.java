package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Manager;
import com.baikalsr.queueserver.entity.Status;
import com.baikalsr.queueserver.entity.TicketService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface TicketServiceRepo extends JpaRepository<TicketService, Long> {

    TicketService getFirstByStatus(Status status);

    List<TicketService> getAllByStatusIsNotNull();

    @Query(value =
            "select * " +
                    "from ticket_service ts join ticket_service_managers tsm " +
                    "on (ts.id = tsm.ticket_services_id " +
                    "   and tsm.managers_id = :manager_id " +
                    "   and (ts.status is not null )) ",
            nativeQuery = true)
    List<TicketService> getAllByManagerAndStatusNotNull(Long manager_id);

    TicketService getFirstByBSService(boolean bsservice);

    @Query(value =
            "select * " +
                    "from ticket_service ts " +
                    "order by ts.name ",
            nativeQuery = true)
    List<TicketService> getAllOrderByName();
}
