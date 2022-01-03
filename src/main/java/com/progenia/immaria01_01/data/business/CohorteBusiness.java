/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Gestionnaire;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.GestionnaireRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class GestionnaireBusiness {
    @Autowired
    private final GestionnaireRepository repository;

    public GestionnaireBusiness(GestionnaireRepository repository) {
        this.repository = repository;
    }

    public Optional<Gestionnaire> findById(String codeGestionnaire) {
        return this.repository.findById(codeGestionnaire);
    }

    public List<Gestionnaire> findAll() {
        return this.repository.findAll();
    }

    public Gestionnaire save(Gestionnaire gestionnaire) {
        return this.repository.save(gestionnaire);
    }
            
    public void delete(Gestionnaire gestionnaire) {
        this.repository.delete(gestionnaire);
    }
    
    public List<Gestionnaire> getReportData() {
        return this.repository.findAll();
    } 
}
