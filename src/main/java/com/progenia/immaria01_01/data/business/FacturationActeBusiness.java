/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.FacturationActe;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.FacturationActeRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationActeBusiness {
    @Autowired
    private final FacturationActeRepository repository;

    public FacturationActeBusiness(FacturationActeRepository repository) {
        this.repository = repository;
    }

    public Optional<FacturationActe> findById(Long noFacturation) {
        return this.repository.findById(noFacturation);
    }

    public List<FacturationActe> findAll() {
        return this.repository.findAll();
    }

    public FacturationActe save(FacturationActe facturationActe) {
        return this.repository.save(facturationActe);
    }
            
    public void delete(FacturationActe facturationActe) {
        this.repository.delete(facturationActe);
    }
    
    public List<FacturationActe> getConsultationFacturationActeListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateFacturationBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<FacturationActe> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<FacturationActe> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
