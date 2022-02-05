/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZLocalisation;
import com.progenia.sigdep01_01.data.repository.LocalisationRepository;
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

    public Optional<ZZZLocalisation> findById(String codeLocalisation) {
        return this.repository.findById(codeLocalisation);
    }

    public List<ZZZLocalisation> findAll() {
        return this.repository.findAll();
    }

    public ZZZLocalisation save(ZZZLocalisation localisation) {
        return this.repository.save(localisation);
    }
            
    public void delete(ZZZLocalisation localisation) {
        this.repository.delete(localisation);
    }
    
    public List<ZZZLocalisation> getReportData() {
        return this.repository.findAll();
    } 
}
