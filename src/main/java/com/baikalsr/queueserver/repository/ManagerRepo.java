package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.Manager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ManagerRepository {
    Manager findByRole
}
