/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.CategorieInstrument;
import com.progenia.sigdep01_01.data.repository.CategorieInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CategorieInstrumentBusiness {
    @Autowired
    private final CategorieInstrumentRepository repository;

    public CategorieInstrumentBusiness(CategorieInstrumentRepository repository) {
        this.repository = repository;
    }

    public Optional<CategorieInstrument> findById(String codeCategorieInstrument) {
        return this.repository.findById(codeCategorieInstrument);
    }

    public List<CategorieInstrument> findAll() {
        return this.repository.findAll();
    }

    public CategorieInstrument save(CategorieInstrument categorieInstrument) {
        return this.repository.save(categorieInstrument);
    }
            
    public void delete(CategorieInstrument categorieInstrument) {
        this.repository.delete(categorieInstrument);
    }
    
    public List<CategorieInstrument> getReportData() {
        return this.repository.findAll();
    } 
    
}
