/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemePeriodiciteCommission;
import com.progenia.sigdep01_01.systeme.data.repository.SystemePeriodiciteCommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemePeriodiciteCommissionBusiness {
    @Autowired
    private final SystemePeriodiciteCommissionRepository repository;

    public SystemePeriodiciteCommissionBusiness(SystemePeriodiciteCommissionRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemePeriodiciteCommission> findById(String codePeriodicite) {
        return this.repository.findById(codePeriodicite);
    }

    public List<SystemePeriodiciteCommission> findAll() {
        return this.repository.findAll();
    }

    public SystemePeriodiciteCommission save(SystemePeriodiciteCommission periodiciteCommission) {
        return this.repository.save(periodiciteCommission);
    }
            
    public void delete(SystemePeriodiciteCommission periodiciteCommission) {
        this.repository.delete(periodiciteCommission);
    } 
}
