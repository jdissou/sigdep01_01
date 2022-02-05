/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeMethodeDecaissement;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeMethodeDecaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeMethodeDecaissementBusiness {
    @Autowired
    private final SystemeMethodeDecaissementRepository repository;

    public SystemeMethodeDecaissementBusiness(SystemeMethodeDecaissementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeMethodeDecaissement> findById(String codeMethodeDecaissement) {
        return this.repository.findById(codeMethodeDecaissement);
    }

    public List<SystemeMethodeDecaissement> findAll() {
        return this.repository.findAll();
    }

    public SystemeMethodeDecaissement save(SystemeMethodeDecaissement methodeDecaissement) {
        return this.repository.save(methodeDecaissement);
    }
            
    public void delete(SystemeMethodeDecaissement methodeDecaissement) {
        this.repository.delete(methodeDecaissement);
    } 
}
