/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemePeriodiciteRemboursement;
import com.progenia.sigdep01_01.systeme.data.repository.SystemePeriodiciteRemboursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemePeriodiciteRemboursementBusiness {
    @Autowired
    private final SystemePeriodiciteRemboursementRepository repository;

    public SystemePeriodiciteRemboursementBusiness(SystemePeriodiciteRemboursementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemePeriodiciteRemboursement> findById(String codePeriodicite) {
        return this.repository.findById(codePeriodicite);
    }

    public List<SystemePeriodiciteRemboursement> findAll() {
        return this.repository.findAll();
    }

    public SystemePeriodiciteRemboursement save(SystemePeriodiciteRemboursement periodiciteRemboursement) {
        return this.repository.save(periodiciteRemboursement);
    }
            
    public void delete(SystemePeriodiciteRemboursement periodiciteRemboursement) {
        this.repository.delete(periodiciteRemboursement);
    } 
}
