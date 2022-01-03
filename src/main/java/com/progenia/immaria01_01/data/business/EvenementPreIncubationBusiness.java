/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.EvenementPreIncubation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.EvenementPreIncubationRepository;
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

    public Optional<EvenementPreIncubation> findById(Long noEvenement) {
        return this.repository.findById(noEvenement);
    }

    public List<EvenementPreIncubation> findAll() {
        return this.repository.findAll();
    }

    public EvenementPreIncubation save(EvenementPreIncubation evenementPorteur) {
        return this.repository.save(evenementPorteur);
    }
            
    public void delete(EvenementPreIncubation evenementPorteur) {
        this.repository.delete(evenementPorteur);
    }
    
    public List<EvenementPreIncubation> getConsultationEvenementPreIncubationListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateEvenementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<EvenementPreIncubation> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<EvenementPreIncubation> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
