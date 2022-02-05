/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeTauxInteret;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeTypeTauxInteretRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeTauxInteretBusiness {
    @Autowired
    private final SystemeTypeTauxInteretRepository repository;

    public SystemeTypeTauxInteretBusiness(SystemeTypeTauxInteretRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeTauxInteret> findById(String codeTypeTaux) {
        return this.repository.findById(codeTypeTaux);
    }

    public List<SystemeTypeTauxInteret> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeTauxInteret save(SystemeTypeTauxInteret typeTaux) {
        return this.repository.save(typeTaux);
    }
            
    public void delete(SystemeTypeTauxInteret typeTaux) {
        this.repository.delete(typeTaux);
    }
    
}
