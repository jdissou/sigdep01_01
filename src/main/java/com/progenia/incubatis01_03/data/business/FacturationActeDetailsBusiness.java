/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.FacturationActeDetails;
import com.progenia.incubatis01_03.data.entity.FacturationActeDetailsId;
import com.progenia.incubatis01_03.data.repository.FacturationActeDetailsRepository;
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

    public List<FacturationActeDetails> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationActeDetailsIdNoFacturation(noFacturation);
    }
            
    public List<FacturationActeDetails> getDetailsRelatedDataByNoPrestation(Long noPrestation) {
        return this.repository.findByFacturationActeDetailsIdNoPrestation(noPrestation);
    }
            
    public List<FacturationActeDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<FacturationActeDetails> findById(FacturationActeDetailsId facturationActeDetailsId) {
        return this.repository.findById(facturationActeDetailsId);
    }

    public Optional<FacturationActeDetails> getFacturationActeDetails(Long noFacturation, Long noPrestation) {
        
        FacturationActeDetailsId facturationActeDetailsId = new FacturationActeDetailsId();
        facturationActeDetailsId.setNoFacturation(noFacturation);
        facturationActeDetailsId.setNoPrestation(noPrestation);
            
        return this.repository.findById(facturationActeDetailsId);
    }
    
    public FacturationActeDetails save(FacturationActeDetails facturationActeDetails) {
        return this.repository.save(facturationActeDetails);
    }
            
    public void delete(FacturationActeDetails facturationActeDetails) {
        this.repository.delete(facturationActeDetails);
    }
}
