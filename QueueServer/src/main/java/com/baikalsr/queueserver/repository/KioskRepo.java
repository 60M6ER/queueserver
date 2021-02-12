package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Kiosk;
import com.baikalsr.queueserver.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface KioskRepo extends JpaRepository<Kiosk, Long> {
    Kiosk findByIP(String IP);

    List<Kiosk> findAllByQueueAndTestOrderById(Queue queue, boolean test);

    @Query(value = "SELECT * " +
            "FROM kiosk k " +
            "WHERE k.queue_id = :queueId " +
            "AND k.test = false " +
            "AND k.name != 'Незарегистрированное устройство';",
            nativeQuery = true)
    List<Kiosk> findAllByQueueForAdminPage(@Param("queueId") Long queueId);
}
