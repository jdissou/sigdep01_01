/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.Decaissement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.DecaissementRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class DecaissementBusiness {
    @Autowired
    private final DecaissementRepository repository;

    public DecaissementBusiness(DecaissementRepository repository) {
        this.repository = repository;
    }

    public Optional<Decaissement> findById(Long noDecaissement) {
        return this.repository.findById(noDecaissement);
    }

    public List<Decaissement> findAll() {
        return this.repository.findAll();
    }

    public Decaissement save(Decaissement decaissement) {
        return this.repository.save(decaissement);
    }
            
    public void delete(Decaissement decaissement) {
        this.repository.delete(decaissement);
    }
    
    public List<Decaissement> getConsultationDecaissementListe(LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndDateDecaissementBetween(true, startDate, endDate);
    }
    
    public List<Decaissement> getBrouillardData() {
        return this.repository.findByIsSaisieValidee(false);
        //return this.repository.getBrouillardData();
    } 
    
    public List<Decaissement> getValidatedData() {
        return this.repository.findByIsSaisieValidee(true);
        //return this.repository.getValidatedData();
    } 
}
