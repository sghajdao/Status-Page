package com.status.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.status.entities.SwitchEntity;

@Repository
public interface  SwitchRepository extends JpaRepository<SwitchEntity, Long> {
    Optional<SwitchEntity> findByIp(String ip);
}
