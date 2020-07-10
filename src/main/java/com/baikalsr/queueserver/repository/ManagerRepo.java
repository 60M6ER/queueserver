package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {
    Manager findByName(String name);
}
