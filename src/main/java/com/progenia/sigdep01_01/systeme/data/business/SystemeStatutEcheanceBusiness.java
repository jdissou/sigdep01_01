/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeStatutEcheance;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeStatutEcheanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeStatutEcheanceBusiness {
    @Autowired
    private final SystemeStatutEcheanceRepository repository;

    public SystemeStatutEcheanceBusiness(SystemeStatutEcheanceRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeStatutEcheance> findById(String codeStatutEcheance) {
        return this.repository.findById(codeStatutEcheance);
    }

    public List<SystemeStatutEcheance> findAll() {
        return this.repository.findAll();
    }

    public SystemeStatutEcheance save(SystemeStatutEcheance statutEcheance) {
        return this.repository.save(statutEcheance);
    }
            
    public void delete(SystemeStatutEcheance statutEcheance) {
        this.repository.delete(statutEcheance);
    } 
}
