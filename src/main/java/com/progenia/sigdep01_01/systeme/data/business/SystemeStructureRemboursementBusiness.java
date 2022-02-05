/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeStructureRemboursement;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeStructureRemboursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeStructureRemboursementBusiness {
    @Autowired
    private final SystemeStructureRemboursementRepository repository;

    public SystemeStructureRemboursementBusiness(SystemeStructureRemboursementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeStructureRemboursement> findById(String codeStructureRemboursement) {
        return this.repository.findById(codeStructureRemboursement);
    }

    public List<SystemeStructureRemboursement> findAll() {
        return this.repository.findAll();
    }

    public SystemeStructureRemboursement save(SystemeStructureRemboursement structureRemboursement) {
        return this.repository.save(structureRemboursement);
    }
            
    public void delete(SystemeStructureRemboursement structureRemboursement) {
        this.repository.delete(structureRemboursement);
    } 
}
