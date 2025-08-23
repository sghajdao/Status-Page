package com.status.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.status.entities.SwitchEntity;
import com.status.repositories.SwitchRepository;

@Service
public class SwitchService {
    @Autowired
    private SwitchRepository switchRepository;

    public SwitchEntity newSwitch(SwitchEntity request) {
        return switchRepository.save(request);
    }

    public List<SwitchEntity> getAll() {
        return switchRepository.findAll();
    }

    public SwitchEntity updateSwitch(SwitchEntity request) {
        SwitchEntity old = switchRepository.findById(request.getId()).orElse(null);
        if (old == null)
            return null;
        old.setIp(request.getIp());
        old.setLocation(request.getLocation());
        return switchRepository.save(old);
    }

    public Boolean deleteSwitch(Long id) {
        SwitchEntity old = switchRepository.findById(id).orElse(null);
        if (old == null)
            return false;
        switchRepository.delete(old);
        return true;
    }
}
