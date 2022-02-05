/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ObjetDette;
import com.progenia.sigdep01_01.data.repository.ObjetDetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ObjetDetteBusiness {
    @Autowired
    private final ObjetDetteRepository repository;

    public ObjetDetteBusiness(ObjetDetteRepository repository) {
        this.repository = repository;
    }

    public Optional<ObjetDette> findById(String codeObjetDette) {
        return this.repository.findById(codeObjetDette);
    }

    public List<ObjetDette> findAll() {
        return this.repository.findAll();
    }

    public ObjetDette save(ObjetDette objetDette) {
        return this.repository.save(objetDette);
    }
            
    public void delete(ObjetDette objetDette) {
        this.repository.delete(objetDette);
    }
    
    public List<ObjetDette> getReportData() {
        return this.repository.findAll();
    } 
}
