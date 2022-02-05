/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnementConsommation;
import com.progenia.sigdep01_01.data.repository.FacturationAbonnementConsommationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationAbonnementConsommationBusiness {
    @Autowired
    private final FacturationAbonnementConsommationRepository repository;

    public FacturationAbonnementConsommationBusiness(FacturationAbonnementConsommationRepository repository) {
        this.repository = repository;
    }

    public List<ZZZFacturationAbonnementConsommation> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationAbonnementConsommationIdNoFacturation(noFacturation);
    }
            
    public List<ZZZFacturationAbonnementConsommation> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByFacturationAbonnementConsommationIdNoInstrument(noInstrument);
    }
            
    public List<ZZZFacturationAbonnementConsommation> getDetailsRelatedDataByCodeService(String codeService) {
        return this.repository.findByFacturationAbonnementConsommationIdCodeService(codeService);
    }
            
    public List<ZZZFacturationAbonnementConsommation> getDetailsRelatedDataByCodeVariable(String codeVariable) {
        return this.repository.findByFacturationAbonnementConsommationIdCodeVariable(codeVariable);
    }
            
    public List<ZZZFacturationAbonnementConsommation> findAll() {
        return this.repository.findAll();
    }

    public List<ZZZFacturationAbonnementConsommation> getFacturationAbonnementConsommationByIsConsommationValideeAndCentreIncubateurAndNoFacturationAndInstrumentAndServiceFourniAndVariableService(Boolean isConsommationValidee, ZZZCentreIncubateur centreIncubateur, Long noFacturation, Instrument Instrument, ServiceFourni serviceFourni, VariableService variableService) {
        return this.repository.findByFacturationAbonnement_IsConsommationValideeAndFacturationAbonnement_CentreIncubateurAndFacturationAbonnement_NoFacturationAndInstrumentAndServiceFourniAndVariableService(isConsommationValidee, centreIncubateur, noFacturation, Instrument, serviceFourni, variableService);
    }

    public Optional<ZZZFacturationAbonnementConsommation> findById(ZZZFacturationAbonnementConsommationId facturationAbonnementConsommationId) {
        return this.repository.findById(facturationAbonnementConsommationId);
    }

    public Optional<ZZZFacturationAbonnementConsommation> getFacturationAbonnementConsommation(Long noFacturation, String noInstrument, String codeService, String codeVariable) {
        
        ZZZFacturationAbonnementConsommationId facturationAbonnementConsommationId = new ZZZFacturationAbonnementConsommationId();
        facturationAbonnementConsommationId.setNoFacturation(noFacturation);
        facturationAbonnementConsommationId.setNoInstrument(noInstrument);
        facturationAbonnementConsommationId.setCodeService(codeService);
        facturationAbonnementConsommationId.setCodeVariable(codeVariable);
            
        return this.repository.findById(facturationAbonnementConsommationId);
    }


    public ZZZFacturationAbonnementConsommation save(ZZZFacturationAbonnementConsommation facturationAbonnementConsommation) {
        return this.repository.save(facturationAbonnementConsommation);
    }
            
    public void delete(ZZZFacturationAbonnementConsommation facturationAbonnementConsommation) {
        this.repository.delete(facturationAbonnementConsommation);
    }
}
