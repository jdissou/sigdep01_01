/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeTypeFacture;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeTypeFactureRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeFactureBusiness {
    @Autowired
    private final SystemeTypeFactureRepository repository;

    public SystemeTypeFactureBusiness(SystemeTypeFactureRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeFacture> findById(String codeTypeFacture) {
        return this.repository.findById(codeTypeFacture);
    }

    public List<SystemeTypeFacture> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeFacture save(SystemeTypeFacture typeFacture) {
        return this.repository.save(typeFacture);
    }
            
    public void delete(SystemeTypeFacture typeFacture) {
        this.repository.delete(typeFacture);
    }
    
}
