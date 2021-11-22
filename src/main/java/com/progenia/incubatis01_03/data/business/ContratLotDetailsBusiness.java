/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.ContratLotDetails;
import com.progenia.incubatis01_03.data.entity.ContratLotDetailsId;
import com.progenia.incubatis01_03.data.repository.ContratLotDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ContratLotDetailsBusiness {
    @Autowired
    private final ContratLotDetailsRepository repository;

    public ContratLotDetailsBusiness(ContratLotDetailsRepository repository) {
        this.repository = repository;
    }

    public List<ContratLotDetails> getDetailsRelatedDataByNoContrat(Long noContrat) {
        return this.repository.findByContratLotDetailsIdNoContrat(noContrat);
    }
            
    public List<ContratLotDetails> getDetailsRelatedDataByNoPorteur(String noPorteur) {
        return this.repository.findByContratLotDetailsIdNoPorteur(noPorteur);
    }
            
    public List<ContratLotDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ContratLotDetails> findById(ContratLotDetailsId contratLotDetailsId) {
        return this.repository.findById(contratLotDetailsId);
    }

     public Optional<ContratLotDetails> getContratLotDetails(Long noContrat, String noPorteur) {
        
        ContratLotDetailsId contratLotDetailsId = new ContratLotDetailsId();
        contratLotDetailsId.setNoContrat(noContrat);
        contratLotDetailsId.setNoPorteur(noPorteur);
            
        return this.repository.findById(contratLotDetailsId);
    }
    
    public ContratLotDetails save(ContratLotDetails contratLotDetails) {
        return this.repository.save(contratLotDetails);
    }
            
    public void delete(ContratLotDetails contratLotDetails) {
        this.repository.delete(contratLotDetails);
    }
}
