package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.ManagersStatus;
import com.baikalsr.queueserver.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Repository
public interface ManagersStatusRepo extends JpaRepository<ManagersStatus, Long> {

    @Query(value = "SELECT * FROM managers_status m where m.manager_id = :managerId order by m.date desc limit 1", nativeQuery = true)
    ManagersStatus getLastByManagerId(@Param("managerId") Long managerId);

    @Query(value = "SELECT " +
            "   ms.id," +
            "   ms.casement," +
            "   ms.date," +
            "   ms.manager_id," +
            "   ms.status " +
            "FROM managers_status ms join manager m " +
            "on(ms.manager_id = m.id " +
            "   and m.queue_id = :queueId " +
            "   and ms.casement = :casement) " +
            "ORDER BY ms.date DESC LIMIT 1",
            nativeQuery = true)
    ManagersStatus getLastByCasementAndQueue(@Param("casement") int casement,
                                                  @Param("queueId") Long queueId);

    @Query(value = "WITH manager_status_last AS (\n" +
            "    SELECT MAX(id) AS id\n" +
            "    FROM managers_status\n" +
            "    GROUP BY manager_id\n" +
            ")\n" +
            "\n" +
            "SELECT *\n" +
            "FROM managers_status m\n" +
            "WHERE m.id in (SELECT id FROM manager_status_last)\n" +
            "    AND m.date <= :date \n" +
            "    AND m.status = :status limit 100", nativeQuery = true)
    List<ManagersStatus> getAllByOlderDateAndStatus(@Param("date") Date date, @Param("status") String status);

}
