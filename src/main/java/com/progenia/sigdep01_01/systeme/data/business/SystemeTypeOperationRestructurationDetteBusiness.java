/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeOperationRestructurationDette;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeTypeOperationRestructurationDetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeOperationRestructurationDetteBusiness {
    @Autowired
    private final SystemeTypeOperationRestructurationDetteRepository repository;

    public SystemeTypeOperationRestructurationDetteBusiness(SystemeTypeOperationRestructurationDetteRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeOperationRestructurationDette> findById(String codeTypeOperation) {
        return this.repository.findById(codeTypeOperation);
    }

    public List<SystemeTypeOperationRestructurationDette> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeOperationRestructurationDette save(SystemeTypeOperationRestructurationDette typeOperation) {
        return this.repository.save(typeOperation);
    }
            
    public void delete(SystemeTypeOperationRestructurationDette typeOperation) {
        this.repository.delete(typeOperation);
    }
    
}
