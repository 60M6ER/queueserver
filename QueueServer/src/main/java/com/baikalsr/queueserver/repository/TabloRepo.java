package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Queue;
import com.baikalsr.queueserver.entity.Tablo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Repository
public interface TabloRepo extends JpaRepository<Tablo, Long> {
    Tablo findByIP(String IP);

    List<Tablo> findAllByQueueOrderById(Queue queue);

    @Query(value = "SELECT * " +
            "FROM tablo t " +
            "WHERE t.queue_id = :queueId " +
            "AND t.test = false " +
            "AND t.name != 'Незарегистрированное устройство';",
            nativeQuery = true)
    List<Tablo> findAllByQueueForAdmin(@Param("queueId") Long queueId);
}
