/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeEcheanceInitiale;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeEcheanceInitialeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeEcheanceInitialeBusiness {
    @Autowired
    private final SystemeEcheanceInitialeRepository repository;

    public SystemeEcheanceInitialeBusiness(SystemeEcheanceInitialeRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeEcheanceInitiale> findById(String codeEcheanceInitiale) {
        return this.repository.findById(codeEcheanceInitiale);
    }

    public List<SystemeEcheanceInitiale> findAll() {
        return this.repository.findAll();
    }

    public SystemeEcheanceInitiale save(SystemeEcheanceInitiale echeanceInitiale) {
        return this.repository.save(echeanceInitiale);
    }
            
    public void delete(SystemeEcheanceInitiale echeanceInitiale) {
        this.repository.delete(echeanceInitiale);
    } 
}
