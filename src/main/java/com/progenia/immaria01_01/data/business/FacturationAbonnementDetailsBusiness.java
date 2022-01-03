/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.FacturationAbonnementDetails;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementDetailsId;
import com.progenia.immaria01_01.data.repository.FacturationAbonnementDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationAbonnementDetailsBusiness {
    @Autowired
    private final FacturationAbonnementDetailsRepository repository;

    public FacturationAbonnementDetailsBusiness(FacturationAbonnementDetailsRepository repository) {
        this.repository = repository;
    }

    public List<FacturationAbonnementDetails> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationAbonnementDetailsIdNoFacturation(noFacturation);
    }
            
    public List<FacturationAbonnementDetails> getDetailsRelatedDataByNoPorteur(String noPorteur) {
        return this.repository.findByFacturationAbonnementDetailsIdNoPorteur(noPorteur);
    }
            
    public List<FacturationAbonnementDetails> getDetailsRelatedDataByCodeService(String codeService) {
        return this.repository.findByFacturationAbonnementDetailsIdCodeService(codeService);
    }
            
    public List<FacturationAbonnementDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<FacturationAbonnementDetails> findById(FacturationAbonnementDetailsId facturationAbonnementDetailsId) {
        return this.repository.findById(facturationAbonnementDetailsId);
    }

    public Optional<FacturationAbonnementDetails> getFacturationAbonnementDetails(Long noFacturation, String noPorteur, String codeService) {
        
        FacturationAbonnementDetailsId facturationAbonnementDetailsId = new FacturationAbonnementDetailsId();
        facturationAbonnementDetailsId.setNoFacturation(noFacturation);
        facturationAbonnementDetailsId.setNoPorteur(noPorteur);
        facturationAbonnementDetailsId.setCodeService(codeService);
            
        return this.repository.findById(facturationAbonnementDetailsId);
    }
    
    public FacturationAbonnementDetails save(FacturationAbonnementDetails facturationAbonnementDetails) {
        return this.repository.save(facturationAbonnementDetails);
    }
            
    public void delete(FacturationAbonnementDetails facturationAbonnementDetails) {
        this.repository.delete(facturationAbonnementDetails);
    }
}
