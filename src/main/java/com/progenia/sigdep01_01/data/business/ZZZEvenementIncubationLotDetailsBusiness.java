/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZEvenementIncubationLotDetails;
import com.progenia.sigdep01_01.data.entity.ZZZEvenementIncubationLotDetailsId;
import com.progenia.sigdep01_01.data.repository.EvenementIncubationLotDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class EvenementIncubationLotDetailsBusiness {
    @Autowired
    private final EvenementIncubationLotDetailsRepository repository;

    public EvenementIncubationLotDetailsBusiness(EvenementIncubationLotDetailsRepository repository) {
        this.repository = repository;
    }

    public List<ZZZEvenementIncubationLotDetails> getDetailsRelatedDataByNoEvenement(Long noEvenement) {
        return this.repository.findByEvenementIncubationLotDetailsIdNoEvenement(noEvenement);
    }
            
    public List<ZZZEvenementIncubationLotDetails> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByEvenementIncubationLotDetailsIdNoInstrument(noInstrument);
    }
            
    public List<ZZZEvenementIncubationLotDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ZZZEvenementIncubationLotDetails> findById(ZZZEvenementIncubationLotDetailsId evenementIncubationLotDetailsId) {
        return this.repository.findById(evenementIncubationLotDetailsId);
    }

    public Optional<ZZZEvenementIncubationLotDetails> getEvenementIncubationLotDetails(Long noEvenement, String noInstrument) {
        
        ZZZEvenementIncubationLotDetailsId evenementIncubationLotDetailsId = new ZZZEvenementIncubationLotDetailsId();
        evenementIncubationLotDetailsId.setNoEvenement(noEvenement);
        evenementIncubationLotDetailsId.setNoInstrument(noInstrument);
            
        return this.repository.findById(evenementIncubationLotDetailsId);
    }
    
    public ZZZEvenementIncubationLotDetails save(ZZZEvenementIncubationLotDetails evenementIncubationLotDetails) {
        return this.repository.save(evenementIncubationLotDetails);
    }
            
    public void delete(ZZZEvenementIncubationLotDetails evenementIncubationLotDetails) {
        this.repository.delete(evenementIncubationLotDetails);
    }
}
