/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeDecaissement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeModeDecaissementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeDecaissementBusiness {
    @Autowired
    private final SystemeModeDecaissementRepository repository;

    public SystemeModeDecaissementBusiness(SystemeModeDecaissementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeDecaissement> findById(String codeModeDecaissement) {
        return this.repository.findById(codeModeDecaissement);
    }

    public List<SystemeModeDecaissement> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeDecaissement save(SystemeModeDecaissement modeDecaissement) {
        return this.repository.save(modeDecaissement);
    }
            
    public void delete(SystemeModeDecaissement modeDecaissement) {
        this.repository.delete(modeDecaissement);
    } 
}
