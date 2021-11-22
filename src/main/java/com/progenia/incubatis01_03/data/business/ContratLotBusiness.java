/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.ContratLot;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.ContratLotRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ContratLotBusiness {
    @Autowired
    private final ContratLotRepository repository;

    public ContratLotBusiness(ContratLotRepository repository) {
        this.repository = repository;
    }

    public Optional<ContratLot> findById(Long noContrat) {
        return this.repository.findById(noContrat);
    }

    public List<ContratLot> findAll() {
        return this.repository.findAll();
    }

    public ContratLot save(ContratLot contratLot) {
        return this.repository.save(contratLot);
    }
            
    public void delete(ContratLot contratLot) {
        this.repository.delete(contratLot);
    }
    
    public List<ContratLot> getConsultationContratLotListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateContratBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ContratLot> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<ContratLot> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
