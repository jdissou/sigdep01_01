/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.EvenementIncubationLot;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.EvenementIncubationLotRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class EvenementIncubationLotBusiness {
    @Autowired
    private final EvenementIncubationLotRepository repository;

    public EvenementIncubationLotBusiness(EvenementIncubationLotRepository repository) {
        this.repository = repository;
    }

    public Optional<EvenementIncubationLot> findById(Long noEvenement) {
        return this.repository.findById(noEvenement);
    }

    public List<EvenementIncubationLot> findAll() {
        return this.repository.findAll();
    }

    public EvenementIncubationLot save(EvenementIncubationLot evenementIncubationLot) {
        return this.repository.save(evenementIncubationLot);
    }
            
    public void delete(EvenementIncubationLot evenementIncubationLot) {
        this.repository.delete(evenementIncubationLot);
    }
    
    public List<EvenementIncubationLot> getConsultationEvenementIncubationLotListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateEvenementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<EvenementIncubationLot> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<EvenementIncubationLot> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
