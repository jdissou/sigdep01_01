/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZFacturationActeDetails;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationActeDetailsId;
import com.progenia.sigdep01_01.data.repository.FacturationActeDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationActeDetailsBusiness {
    @Autowired
    private final FacturationActeDetailsRepository repository;

    public FacturationActeDetailsBusiness(FacturationActeDetailsRepository repository) {
        this.repository = repository;
    }

    public List<ZZZFacturationActeDetails> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationActeDetailsIdNoFacturation(noFacturation);
    }
            
    public List<ZZZFacturationActeDetails> getDetailsRelatedDataByNoPrestation(Long noPrestation) {
        return this.repository.findByFacturationActeDetailsIdNoPrestation(noPrestation);
    }
            
    public List<ZZZFacturationActeDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ZZZFacturationActeDetails> findById(ZZZFacturationActeDetailsId facturationActeDetailsId) {
        return this.repository.findById(facturationActeDetailsId);
    }

    public Optional<ZZZFacturationActeDetails> getFacturationActeDetails(Long noFacturation, Long noPrestation) {
        
        ZZZFacturationActeDetailsId facturationActeDetailsId = new ZZZFacturationActeDetailsId();
        facturationActeDetailsId.setNoFacturation(noFacturation);
        facturationActeDetailsId.setNoPrestation(noPrestation);
            
        return this.repository.findById(facturationActeDetailsId);
    }
    
    public ZZZFacturationActeDetails save(ZZZFacturationActeDetails facturationActeDetails) {
        return this.repository.save(facturationActeDetails);
    }
            
    public void delete(ZZZFacturationActeDetails facturationActeDetails) {
        this.repository.delete(facturationActeDetails);
    }
}
