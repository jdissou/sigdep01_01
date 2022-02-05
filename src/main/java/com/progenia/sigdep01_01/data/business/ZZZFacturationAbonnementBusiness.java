/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnement;
import com.progenia.sigdep01_01.data.entity.IndiceReference;
import com.progenia.sigdep01_01.data.entity.SequenceFacturation;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.FacturationAbonnementRepository;
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

    public Optional<ZZZFacturationAbonnement> findById(Long noFacturation) {
        return this.repository.findById(noFacturation);
    }

    public List<ZZZFacturationAbonnement> findAll() {
        return this.repository.findAll();
    }

    public ZZZFacturationAbonnement save(ZZZFacturationAbonnement facturationAbonnement) {
        return this.repository.save(facturationAbonnement);
    }
            
    public void delete(ZZZFacturationAbonnement facturationAbonnement) {
        this.repository.delete(facturationAbonnement);
    }
    
    public List<ZZZFacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturationAndDateFacturationBetween(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturationAndDateFacturationBetween(codeCentreIncubateur, noPeriode, codeSequenceFacturation, startDate, endDate);
    }
    
    public List<ZZZFacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation) {
        return this.repository.findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(codeCentreIncubateur, noPeriode, codeSequenceFacturation);
    }
    
    public List<ZZZFacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturationAndDateFacturationBetween(ZZZCentreIncubateur centreIncubateur, IndiceReference periodeFacturation, SequenceFacturation sequenceFacturation, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturationAndDateFacturationBetween(centreIncubateur, periodeFacturation, sequenceFacturation, startDate, endDate);
    }
    
    public List<ZZZFacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(ZZZCentreIncubateur centreIncubateur, IndiceReference periodeFacturation, SequenceFacturation sequenceFacturation) {
        return this.repository.findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(centreIncubateur, periodeFacturation, sequenceFacturation);
    }
}
