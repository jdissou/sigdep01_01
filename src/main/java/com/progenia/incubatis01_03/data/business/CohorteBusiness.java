/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.Cohorte;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.CohorteRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CohorteBusiness {
    @Autowired
    private final CohorteRepository repository;

    public CohorteBusiness(CohorteRepository repository) {
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
