/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.ContratAccompagnement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ContratAccompagnementRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ContratAccompagnementBusiness {
    @Autowired
    private final ContratAccompagnementRepository repository;

    public ContratAccompagnementBusiness(ContratAccompagnementRepository repository) {
        this.repository = repository;
    }

    public Optional<ContratAccompagnement> findById(Long noContrat) {
        return this.repository.findById(noContrat);
    }

    public List<ContratAccompagnement> findAll() {
        return this.repository.findAll();
    }

    public ContratAccompagnement save(ContratAccompagnement contratAccompagnement) {
        return this.repository.save(contratAccompagnement);
    }
            
    public void delete(ContratAccompagnement contratAccompagnement) {
        this.repository.delete(contratAccompagnement);
    }
    
    public List<ContratAccompagnement> getConsultationContratAccompagnementListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateContratBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ContratAccompagnement> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
    } 
    
    public List<ContratAccompagnement> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
    } 
}
