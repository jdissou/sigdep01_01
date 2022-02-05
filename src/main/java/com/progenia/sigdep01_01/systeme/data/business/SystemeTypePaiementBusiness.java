/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypePaiement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeTypePaiementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypePaiementBusiness {
    @Autowired
    private final SystemeTypePaiementRepository repository;

    public SystemeTypePaiementBusiness(SystemeTypePaiementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypePaiement> findById(String codeTypePaiement) {
        return this.repository.findById(codeTypePaiement);
    }

    public List<SystemeTypePaiement> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypePaiement save(SystemeTypePaiement typePaiement) {
        return this.repository.save(typePaiement);
    }
            
    public void delete(SystemeTypePaiement typePaiement) {
        this.repository.delete(typePaiement);
    }
    
}
