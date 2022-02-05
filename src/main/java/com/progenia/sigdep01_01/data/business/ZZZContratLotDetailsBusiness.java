/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZContratLotDetails;
import com.progenia.sigdep01_01.data.entity.ZZZContratLotDetailsId;
import com.progenia.sigdep01_01.data.repository.ContratLotDetailsRepository;
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

    public List<ZZZContratLotDetails> getDetailsRelatedDataByNoContrat(Long noContrat) {
        return this.repository.findByContratLotDetailsIdNoContrat(noContrat);
    }
            
    public List<ZZZContratLotDetails> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByContratLotDetailsIdNoInstrument(noInstrument);
    }
            
    public List<ZZZContratLotDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ZZZContratLotDetails> findById(ZZZContratLotDetailsId contratLotDetailsId) {
        return this.repository.findById(contratLotDetailsId);
    }

     public Optional<ZZZContratLotDetails> getContratLotDetails(Long noContrat, String noInstrument) {
        
        ZZZContratLotDetailsId contratLotDetailsId = new ZZZContratLotDetailsId();
        contratLotDetailsId.setNoContrat(noContrat);
        contratLotDetailsId.setNoInstrument(noInstrument);
            
        return this.repository.findById(contratLotDetailsId);
    }
    
    public ZZZContratLotDetails save(ZZZContratLotDetails contratLotDetails) {
        return this.repository.save(contratLotDetails);
    }
            
    public void delete(ZZZContratLotDetails contratLotDetails) {
        this.repository.delete(contratLotDetails);
    }
}
