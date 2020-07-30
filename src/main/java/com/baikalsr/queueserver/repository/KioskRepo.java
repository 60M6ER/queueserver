package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Kiosk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface KioskRepo extends JpaRepository<Kiosk, Long> {
    Kiosk findByIP(String IP);
}
