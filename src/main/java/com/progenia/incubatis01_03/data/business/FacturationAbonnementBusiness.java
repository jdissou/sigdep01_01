/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnement;
import com.progenia.incubatis01_03.data.entity.PeriodeFacturation;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.FacturationAbonnementRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationAbonnementBusiness {
    @Autowired
    private final FacturationAbonnementRepository repository;

    public FacturationAbonnementBusiness(FacturationAbonnementRepository repository) {
        this.repository = repository;
    }

    public Optional<FacturationAbonnement> findById(Long noFacturation) {
        return this.repository.findById(noFacturation);
    }

    public List<FacturationAbonnement> findAll() {
        return this.repository.findAll();
    }

    public FacturationAbonnement save(FacturationAbonnement facturationAbonnement) {
        return this.repository.save(facturationAbonnement);
    }
            
    public void delete(FacturationAbonnement facturationAbonnement) {
        this.repository.delete(facturationAbonnement);
    }
    
    public List<FacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturationAndDateFacturationBetween(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturationAndDateFacturationBetween(codeCentreIncubateur, noPeriode, codeSequenceFacturation, startDate, endDate);
    }
    
    public List<FacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation) {
        return this.repository.findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(codeCentreIncubateur, noPeriode, codeSequenceFacturation);
    }
    
    public List<FacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturationAndDateFacturationBetween(CentreIncubateur centreIncubateur, PeriodeFacturation periodeFacturation, SequenceFacturation sequenceFacturation, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturationAndDateFacturationBetween(centreIncubateur, periodeFacturation, sequenceFacturation, startDate, endDate);
    }
    
    public List<FacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(CentreIncubateur centreIncubateur, PeriodeFacturation periodeFacturation, SequenceFacturation sequenceFacturation) {
        return this.repository.findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(centreIncubateur, periodeFacturation, sequenceFacturation);
    }
}
