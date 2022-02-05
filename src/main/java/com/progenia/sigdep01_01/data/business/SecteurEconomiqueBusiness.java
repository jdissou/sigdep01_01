/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.SecteurEconomique;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.SecteurEconomiqueRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SecteurEconomiqueBusiness {
    @Autowired
    private final SecteurEconomiqueRepository repository;

    public SecteurEconomiqueBusiness(SecteurEconomiqueRepository repository) {
        this.repository = repository;
    }

    public Optional<SecteurEconomique> findById(String codeSecteurEconomique) {
        return this.repository.findById(codeSecteurEconomique);
    }

    public List<SecteurEconomique> findAll() {
        return this.repository.findAll();
    }

    public SecteurEconomique save(SecteurEconomique secteurEconomique) {
        return this.repository.save(secteurEconomique);
    }
            
    public void delete(SecteurEconomique secteurEconomique) {
        this.repository.delete(secteurEconomique);
    }
    
    public List<SecteurEconomique> getReportData() {
        return this.repository.findAll();
    } 
    
}
