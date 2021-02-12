package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.RunLineText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Repository
public interface RunLineTextRepo extends JpaRepository<RunLineText, UUID> {

    @Query(value = "SELECT * " +
            "FROM run_line_text r " +
            "WHERE r.active = true " +
            "AND (r.start_date <= :curDate AND r.end_date >= :curDate )" +
            " OR r.non_date = true;",
            nativeQuery = true)
    List<RunLineText> findAllForVisible(@Param("curDate") Date currentDate);
}
