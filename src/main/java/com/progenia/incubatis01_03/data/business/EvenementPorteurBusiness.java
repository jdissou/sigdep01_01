/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.EvenementPorteur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.EvenementPorteurRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class EvenementPorteurBusiness {
    @Autowired
    private final EvenementPorteurRepository repository;

    public EvenementPorteurBusiness(EvenementPorteurRepository repository) {
        this.repository = repository;
    }

    public Optional<EvenementPorteur> findById(Long noEvenement) {
        return this.repository.findById(noEvenement);
    }

    public List<EvenementPorteur> findAll() {
        return this.repository.findAll();
    }

    public EvenementPorteur save(EvenementPorteur evenementPorteur) {
        return this.repository.save(evenementPorteur);
    }
            
    public void delete(EvenementPorteur evenementPorteur) {
        this.repository.delete(evenementPorteur);
    }
    
    public List<EvenementPorteur> getConsultationEvenementPorteurListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateEvenementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<EvenementPorteur> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<EvenementPorteur> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
