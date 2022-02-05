/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeProfilDecaissement;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeProfilDecaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeProfilDecaissementBusiness {
    @Autowired
    private final SystemeProfilDecaissementRepository repository;

    public SystemeProfilDecaissementBusiness(SystemeProfilDecaissementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeProfilDecaissement> findById(String codeProfilDecaissement) {
        return this.repository.findById(codeProfilDecaissement);
    }

    public List<SystemeProfilDecaissement> findAll() {
        return this.repository.findAll();
    }

    public SystemeProfilDecaissement save(SystemeProfilDecaissement profilDecaissement) {
        return this.repository.save(profilDecaissement);
    }
            
    public void delete(SystemeProfilDecaissement profilDecaissement) {
        this.repository.delete(profilDecaissement);
    } 
}
