/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeBaseAnnuelleCalculTaux;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeBaseAnnuelleCalculTauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeBaseAnnuelleCalculTauxBusiness {
    @Autowired
    private final SystemeBaseAnnuelleCalculTauxRepository repository;

    public SystemeBaseAnnuelleCalculTauxBusiness(SystemeBaseAnnuelleCalculTauxRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeBaseAnnuelleCalculTaux> findById(String codeBaseAnnuelle) {
        return this.repository.findById(codeBaseAnnuelle);
    }

    public List<SystemeBaseAnnuelleCalculTaux> findAll() {
        return this.repository.findAll();
    }

    public SystemeBaseAnnuelleCalculTaux save(SystemeBaseAnnuelleCalculTaux baseAnnuelleCalculTaux) {
        return this.repository.save(baseAnnuelleCalculTaux);
    }
            
    public void delete(SystemeBaseAnnuelleCalculTaux baseAnnuelleCalculTaux) {
        this.repository.delete(baseAnnuelleCalculTaux);
    } 
}
