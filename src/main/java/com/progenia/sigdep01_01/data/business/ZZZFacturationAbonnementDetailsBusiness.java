/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnementDetails;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnementDetailsId;
import com.progenia.sigdep01_01.data.repository.FacturationAbonnementDetailsRepository;
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

    public List<ZZZFacturationAbonnementDetails> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationAbonnementDetailsIdNoFacturation(noFacturation);
    }
            
    public List<ZZZFacturationAbonnementDetails> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByFacturationAbonnementDetailsIdNoInstrument(noInstrument);
    }
            
    public List<ZZZFacturationAbonnementDetails> getDetailsRelatedDataByCodeService(String codeService) {
        return this.repository.findByFacturationAbonnementDetailsIdCodeService(codeService);
    }
            
    public List<ZZZFacturationAbonnementDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ZZZFacturationAbonnementDetails> findById(ZZZFacturationAbonnementDetailsId facturationAbonnementDetailsId) {
        return this.repository.findById(facturationAbonnementDetailsId);
    }

    public Optional<ZZZFacturationAbonnementDetails> getFacturationAbonnementDetails(Long noFacturation, String noInstrument, String codeService) {
        
        ZZZFacturationAbonnementDetailsId facturationAbonnementDetailsId = new ZZZFacturationAbonnementDetailsId();
        facturationAbonnementDetailsId.setNoFacturation(noFacturation);
        facturationAbonnementDetailsId.setNoInstrument(noInstrument);
        facturationAbonnementDetailsId.setCodeService(codeService);
            
        return this.repository.findById(facturationAbonnementDetailsId);
    }
    
    public ZZZFacturationAbonnementDetails save(ZZZFacturationAbonnementDetails facturationAbonnementDetails) {
        return this.repository.save(facturationAbonnementDetails);
    }
            
    public void delete(ZZZFacturationAbonnementDetails facturationAbonnementDetails) {
        this.repository.delete(facturationAbonnementDetails);
    }
}
