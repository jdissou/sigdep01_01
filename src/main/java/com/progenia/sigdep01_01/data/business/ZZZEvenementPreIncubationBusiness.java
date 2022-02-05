/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZEvenementPreIncubation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.EvenementPreIncubationRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class EvenementPreIncubationBusiness {
    @Autowired
    private final EvenementPreIncubationRepository repository;

    public EvenementPreIncubationBusiness(EvenementPreIncubationRepository repository) {
        this.repository = repository;
    }

    public Optional<ZZZEvenementPreIncubation> findById(Long noEvenement) {
        return this.repository.findById(noEvenement);
    }

    public List<ZZZEvenementPreIncubation> findAll() {
        return this.repository.findAll();
    }

    public ZZZEvenementPreIncubation save(ZZZEvenementPreIncubation evenementInstrument) {
        return this.repository.save(evenementInstrument);
    }
            
    public void delete(ZZZEvenementPreIncubation evenementInstrument) {
        this.repository.delete(evenementInstrument);
    }
    
    public List<ZZZEvenementPreIncubation> getConsultationEvenementPreIncubationListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateEvenementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ZZZEvenementPreIncubation> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<ZZZEvenementPreIncubation> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(true, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
