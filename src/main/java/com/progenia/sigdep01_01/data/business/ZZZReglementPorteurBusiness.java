/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ReglementInstrument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.ReglementInstrumentRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ReglementInstrumentBusiness {
    @Autowired
    private final ReglementInstrumentRepository repository;

    public ReglementInstrumentBusiness(ReglementInstrumentRepository repository) {
        this.repository = repository;
    }

    public Optional<ReglementInstrument> findById(Long noReglement) {
        return this.repository.findById(noReglement);
    }

    public List<ReglementInstrument> findAll() {
        return this.repository.findAll();
    }

    public ReglementInstrument save(ReglementInstrument reglementInstrument) {
        return this.repository.save(reglementInstrument);
    }
            
    public void delete(ReglementInstrument reglementInstrument) {
        this.repository.delete(reglementInstrument);
    }
    
    public List<ReglementInstrument> getConsultationReglementInstrumentListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateReglementBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<ReglementInstrument> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<ReglementInstrument> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(true, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
