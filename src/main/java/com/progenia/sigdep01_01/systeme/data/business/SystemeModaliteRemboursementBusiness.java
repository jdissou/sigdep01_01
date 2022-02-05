/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeModaliteRemboursement;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeModaliteRemboursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModaliteRemboursementBusiness {
    @Autowired
    private final SystemeModaliteRemboursementRepository repository;

    public SystemeModaliteRemboursementBusiness(SystemeModaliteRemboursementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModaliteRemboursement> findById(String codeModalite) {
        return this.repository.findById(codeModalite);
    }

    public List<SystemeModaliteRemboursement> findAll() {
        return this.repository.findAll();
    }

    public SystemeModaliteRemboursement save(SystemeModaliteRemboursement modaliteRemboursement) {
        return this.repository.save(modaliteRemboursement);
    }
            
    public void delete(SystemeModaliteRemboursement modaliteRemboursement) {
        this.repository.delete(modaliteRemboursement);
    } 
}
