package com.baikalsr.queueserver.repository;

import com.baikalsr.queueserver.entity.SettingName;
import com.baikalsr.queueserver.entity.SettingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface SettingProgramRepo extends JpaRepository<SettingProgram, SettingName> {

    SettingProgram getBySettingName(SettingName settingName);
}
