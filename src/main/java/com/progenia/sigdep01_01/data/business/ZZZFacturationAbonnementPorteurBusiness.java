/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.repository.FacturationAbonnementInstrumentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationAbonnementInstrumentBusiness {
    @Autowired
    private final FacturationAbonnementInstrumentRepository repository;

    public FacturationAbonnementInstrumentBusiness(FacturationAbonnementInstrumentRepository repository) {
        this.repository = repository;
    }

    public List<ZZZFacturationAbonnementInstrument> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationAbonnementInstrumentIdNoFacturation(noFacturation);
    }
            
    public List<ZZZFacturationAbonnementInstrument> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByFacturationAbonnementInstrumentIdNoInstrument(noInstrument);
    }
            
    public List<ZZZFacturationAbonnementInstrument> findAll() {
        return this.repository.findAll();
    }

    public List<ZZZFacturationAbonnementInstrument> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(ZZZCentreIncubateur centreIncubateur, IndiceReference periodeFacturation, SequenceFacturation sequenceFacturation) {
        return this.repository.findByFacturationAbonnementCentreIncubateurAndFacturationAbonnementPeriodeFacturationAndFacturationAbonnementSequenceFacturation(centreIncubateur, periodeFacturation, sequenceFacturation);
    }

    public Optional<ZZZFacturationAbonnementInstrument> findById(ZZZFacturationAbonnementInstrumentId facturationAbonnementInstrumentId) {
        return this.repository.findById(facturationAbonnementInstrumentId);
    }

    public Optional<ZZZFacturationAbonnementInstrument> getFacturationAbonnementInstrument(Long noFacturation, String noInstrument) {
        
        ZZZFacturationAbonnementInstrumentId facturationAbonnementInstrumentId = new ZZZFacturationAbonnementInstrumentId();
        facturationAbonnementInstrumentId.setNoFacturation(noFacturation);
        facturationAbonnementInstrumentId.setNoInstrument(noInstrument);
            
        return this.repository.findById(facturationAbonnementInstrumentId);
    }
    
    public ZZZFacturationAbonnementInstrument save(ZZZFacturationAbonnementInstrument facturationAbonnementInstrument) {
        return this.repository.save(facturationAbonnementInstrument);
    }
            
    public void delete(ZZZFacturationAbonnementInstrument facturationAbonnementInstrument) {
        this.repository.delete(facturationAbonnementInstrument);
    }
}
