package com.linking.push_settings.persistence;

import com.linking.push_settings.domain.PushSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushSettingsRepository extends JpaRepository<PushSettings, Long> {


}
