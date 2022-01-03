/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Cohorte;
import com.progenia.immaria01_01.data.repository.CohorteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class GestionnaireBusiness {
    @Autowired
    private final CohorteRepository repository;

    public GestionnaireBusiness(CohorteRepository repository) {
        this.repository = repository;
    }

    public Optional<Cohorte> findById(String codeCohorte) {
        return this.repository.findById(codeCohorte);
    }

    public List<Cohorte> findAll() {
        return this.repository.findAll();
    }

    public Cohorte save(Cohorte cohorte) {
        return this.repository.save(cohorte);
    }
            
    public void delete(Cohorte cohorte) {
        this.repository.delete(cohorte);
    }
    
    public List<Cohorte> getReportData() {
        return this.repository.findAll();
    } 
}
