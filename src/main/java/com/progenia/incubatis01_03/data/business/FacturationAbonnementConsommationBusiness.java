/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementConsommation;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementConsommationId;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.VariableService;
import com.progenia.incubatis01_03.data.repository.FacturationAbonnementConsommationRepository;
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

    public List<FacturationAbonnementConsommation> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationAbonnementConsommationIdNoFacturation(noFacturation);
    }
            
    public List<FacturationAbonnementConsommation> getDetailsRelatedDataByNoPorteur(String noPorteur) {
        return this.repository.findByFacturationAbonnementConsommationIdNoPorteur(noPorteur);
    }
            
    public List<FacturationAbonnementConsommation> getDetailsRelatedDataByCodeService(String codeService) {
        return this.repository.findByFacturationAbonnementConsommationIdCodeService(codeService);
    }
            
    public List<FacturationAbonnementConsommation> getDetailsRelatedDataByCodeVariable(String codeVariable) {
        return this.repository.findByFacturationAbonnementConsommationIdCodeVariable(codeVariable);
    }
            
    public List<FacturationAbonnementConsommation> findAll() {
        return this.repository.findAll();
    }

    public List<FacturationAbonnementConsommation> getFacturationAbonnementConsommationByIsConsommationValideeAndCentreIncubateurAndNoFacturationAndPorteurAndServiceFourniAndVariableService(Boolean isConsommationValidee, CentreIncubateur centreIncubateur, Long noFacturation, Porteur porteur, ServiceFourni serviceFourni, VariableService variableService) {
        return this.repository.findByFacturationAbonnement_IsConsommationValideeAndFacturationAbonnement_CentreIncubateurAndFacturationAbonnement_NoFacturationAndPorteurAndServiceFourniAndVariableService(isConsommationValidee, centreIncubateur, noFacturation, porteur, serviceFourni, variableService);
    }

    public Optional<FacturationAbonnementConsommation> findById(FacturationAbonnementConsommationId facturationAbonnementConsommationId) {
        return this.repository.findById(facturationAbonnementConsommationId);
    }

    public Optional<FacturationAbonnementConsommation> getFacturationAbonnementConsommation(Long noFacturation, String noPorteur, String codeService, String codeVariable) {
        
        FacturationAbonnementConsommationId facturationAbonnementConsommationId = new FacturationAbonnementConsommationId();
        facturationAbonnementConsommationId.setNoFacturation(noFacturation);
        facturationAbonnementConsommationId.setNoPorteur(noPorteur);
        facturationAbonnementConsommationId.setCodeService(codeService);
        facturationAbonnementConsommationId.setCodeVariable(codeVariable);
            
        return this.repository.findById(facturationAbonnementConsommationId);
    }


    public FacturationAbonnementConsommation save(FacturationAbonnementConsommation facturationAbonnementConsommation) {
        return this.repository.save(facturationAbonnementConsommation);
    }
            
    public void delete(FacturationAbonnementConsommation facturationAbonnementConsommation) {
        this.repository.delete(facturationAbonnementConsommation);
    }
}
