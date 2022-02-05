/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZContratLot;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.ContratLotRepository;
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

    public Optional<ZZZContratLot> findById(Long noContrat) {
        return this.repository.findById(noContrat);
    }

    public List<ZZZContratLot> findAll() {
        return this.repository.findAll();
    }

    public ZZZContratLot save(ZZZContratLot contratLot) {
        return this.repository.save(contratLot);
    }
            
    public void delete(ZZZContratLot contratLot) {
        this.repository.delete(contratLot);
    }
    
    public List<ZZZContratLot> getConsultationContratLotListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateContratBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ZZZContratLot> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<ZZZContratLot> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(true, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
