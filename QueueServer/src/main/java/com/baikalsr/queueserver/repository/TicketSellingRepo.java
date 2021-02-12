package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.TicketSelling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Repository
public interface TicketSellingRepo extends JpaRepository<TicketSelling, UUID> {
}
