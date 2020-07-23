package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.ManagersStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ManagersStatusRepo extends JpaRepository<ManagersStatus, Long> {

    @Query(value = "SELECT * FROM managers_status m where m.manager_id = :managerId order by m.date desc limit 1", nativeQuery = true)
    ManagersStatus getLastByManagerId(@Param("managerId") Long managerId);
}
