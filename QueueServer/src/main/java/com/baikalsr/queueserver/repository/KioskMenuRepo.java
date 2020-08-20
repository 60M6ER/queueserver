package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.KioskMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface KioskMenuRepo extends JpaRepository<KioskMenu, Long> {
}
