package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface QueueRepo extends JpaRepository<Queue, Long> {

}
