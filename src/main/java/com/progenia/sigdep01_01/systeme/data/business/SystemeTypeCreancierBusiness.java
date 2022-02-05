/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeCreancier;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeTypeCreancierRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeCreancierBusiness {
    @Autowired
    private final SystemeTypeCreancierRepository repository;

    public SystemeTypeCreancierBusiness(SystemeTypeCreancierRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeCreancier> findById(String codeTypeCreancier) {
        return this.repository.findById(codeTypeCreancier);
    }

    public List<SystemeTypeCreancier> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeCreancier save(SystemeTypeCreancier typeCreancier) {
        return this.repository.save(typeCreancier);
    }
            
    public void delete(SystemeTypeCreancier typeCreancier) {
        this.repository.delete(typeCreancier);
    }
    
}
