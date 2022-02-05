/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZEvenementIncubationLot;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.EvenementIncubationLotRepository;
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

    public Optional<ZZZEvenementIncubationLot> findById(Long noEvenement) {
        return this.repository.findById(noEvenement);
    }

    public List<ZZZEvenementIncubationLot> findAll() {
        return this.repository.findAll();
    }

    public ZZZEvenementIncubationLot save(ZZZEvenementIncubationLot evenementIncubationLot) {
        return this.repository.save(evenementIncubationLot);
    }
            
    public void delete(ZZZEvenementIncubationLot evenementIncubationLot) {
        this.repository.delete(evenementIncubationLot);
    }
    
    public List<ZZZEvenementIncubationLot> getConsultationEvenementIncubationLotListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateEvenementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ZZZEvenementIncubationLot> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<ZZZEvenementIncubationLot> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(true, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
