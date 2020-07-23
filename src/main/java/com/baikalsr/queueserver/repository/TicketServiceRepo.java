package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.TicketService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface TicketServiceRepo extends JpaRepository<TicketService, Long> {

}
