/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeMouvementFinancier;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeTypeMouvementFinancierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeMouvementFinancierBusiness {
    @Autowired
    private final SystemeTypeMouvementFinancierRepository repository;

    public SystemeTypeMouvementFinancierBusiness(SystemeTypeMouvementFinancierRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeMouvementFinancier> findById(String codeTypeMouvement) {
        return this.repository.findById(codeTypeMouvement);
    }

    public List<SystemeTypeMouvementFinancier> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeMouvementFinancier save(SystemeTypeMouvementFinancier typeMouvement) {
        return this.repository.save(typeMouvement);
    }
            
    public void delete(SystemeTypeMouvementFinancier typeMouvement) {
        this.repository.delete(typeMouvement);
    }
    
}
