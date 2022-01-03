/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.TitreCivilite;
import com.progenia.immaria01_01.data.repository.TitreCiviliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TitreCiviliteBusiness {
    @Autowired
    private final TitreCiviliteRepository repository;

    public TitreCiviliteBusiness(TitreCiviliteRepository repository) {
        this.repository = repository;
    }

    public Optional<TitreCivilite> findById(String codeTitreCivilite) {
        return this.repository.findById(codeTitreCivilite);
    }

    public List<TitreCivilite> findAll() {
        return this.repository.findAll();
    }

    public TitreCivilite save(TitreCivilite TitreCivilite) {
        return this.repository.save(TitreCivilite);
    }
            
    public void delete(TitreCivilite TitreCivilite) {
        this.repository.delete(TitreCivilite);
    }
    
    public List<TitreCivilite> getReportData() {
        return this.repository.findAll();
    } 
    
}
