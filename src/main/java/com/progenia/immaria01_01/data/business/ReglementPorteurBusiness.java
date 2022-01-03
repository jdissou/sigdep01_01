/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.ReglementPorteur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ReglementPorteurRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ReglementPorteurBusiness {
    @Autowired
    private final ReglementPorteurRepository repository;

    public ReglementPorteurBusiness(ReglementPorteurRepository repository) {
        this.repository = repository;
    }

    public Optional<ReglementPorteur> findById(Long noReglement) {
        return this.repository.findById(noReglement);
    }

    public List<ReglementPorteur> findAll() {
        return this.repository.findAll();
    }

    public ReglementPorteur save(ReglementPorteur reglementPorteur) {
        return this.repository.save(reglementPorteur);
    }
            
    public void delete(ReglementPorteur reglementPorteur) {
        this.repository.delete(reglementPorteur);
    }
    
    public List<ReglementPorteur> getConsultationReglementPorteurListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateReglementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ReglementPorteur> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<ReglementPorteur> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
