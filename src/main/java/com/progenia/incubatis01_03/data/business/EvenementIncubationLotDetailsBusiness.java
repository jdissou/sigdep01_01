/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.EvenementIncubationLotDetails;
import com.progenia.incubatis01_03.data.entity.EvenementIncubationLotDetailsId;
import com.progenia.incubatis01_03.data.repository.EvenementIncubationLotDetailsRepository;
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

    public List<EvenementIncubationLotDetails> getDetailsRelatedDataByNoEvenement(Long noEvenement) {
        return this.repository.findByEvenementIncubationLotDetailsIdNoEvenement(noEvenement);
    }
            
    public List<EvenementIncubationLotDetails> getDetailsRelatedDataByNoPorteur(String noPorteur) {
        return this.repository.findByEvenementIncubationLotDetailsIdNoPorteur(noPorteur);
    }
            
    public List<EvenementIncubationLotDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<EvenementIncubationLotDetails> findById(EvenementIncubationLotDetailsId evenementIncubationLotDetailsId) {
        return this.repository.findById(evenementIncubationLotDetailsId);
    }

    public Optional<EvenementIncubationLotDetails> getEvenementIncubationLotDetails(Long noEvenement, String noPorteur) {
        
        EvenementIncubationLotDetailsId evenementIncubationLotDetailsId = new EvenementIncubationLotDetailsId();
        evenementIncubationLotDetailsId.setNoEvenement(noEvenement);
        evenementIncubationLotDetailsId.setNoPorteur(noPorteur);
            
        return this.repository.findById(evenementIncubationLotDetailsId);
    }
    
    public EvenementIncubationLotDetails save(EvenementIncubationLotDetails evenementIncubationLotDetails) {
        return this.repository.save(evenementIncubationLotDetails);
    }
            
    public void delete(EvenementIncubationLotDetails evenementIncubationLotDetails) {
        this.repository.delete(evenementIncubationLotDetails);
    }
}
