/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Localisation;
import com.progenia.immaria01_01.data.repository.LocalisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class LocalisationBusiness {
    @Autowired
    private final LocalisationRepository repository;

    public LocalisationBusiness(LocalisationRepository repository) {
        this.repository = repository;
    }

    public Optional<Localisation> findById(String codeLocalisation) {
        return this.repository.findById(codeLocalisation);
    }

    public List<Localisation> findAll() {
        return this.repository.findAll();
    }

    public Localisation save(Localisation localisation) {
        return this.repository.save(localisation);
    }
            
    public void delete(Localisation localisation) {
        this.repository.delete(localisation);
    }
    
    public List<Localisation> getReportData() {
        return this.repository.findAll();
    } 
}
