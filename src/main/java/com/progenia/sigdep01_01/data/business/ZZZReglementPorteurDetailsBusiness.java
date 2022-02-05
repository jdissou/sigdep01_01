/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ReglementInstrumentDetails;
import com.progenia.sigdep01_01.data.entity.ReglementInstrumentDetailsId;
import com.progenia.sigdep01_01.data.repository.ReglementInstrumentDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ReglementInstrumentDetailsBusiness {
    @Autowired
    private final ReglementInstrumentDetailsRepository repository;

    public ReglementInstrumentDetailsBusiness(ReglementInstrumentDetailsRepository repository) {
        this.repository = repository;
    }

    public List<ReglementInstrumentDetails> getDetailsRelatedDataByNoReglement(Long noReglement) {
        return this.repository.findByReglementInstrumentDetailsIdNoReglement(noReglement);
    }
            
    public List<ReglementInstrumentDetails> getDetailsRelatedDataByNoMouvementFacture(Long noMouvementFacture) {
        return this.repository.findByReglementInstrumentDetailsIdNoMouvementFacture(noMouvementFacture);
    }
            
    public List<ReglementInstrumentDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ReglementInstrumentDetails> findById(ReglementInstrumentDetailsId reglementInstrumentDetailsId) {
        return this.repository.findById(reglementInstrumentDetailsId);
    }

    public Optional<ReglementInstrumentDetails> getReglementInstrumentDetails(Long noReglement, Long noMouvementFacture) {
        
        ReglementInstrumentDetailsId reglementInstrumentDetailsId = new ReglementInstrumentDetailsId();
        reglementInstrumentDetailsId.setNoReglement(noReglement);
        reglementInstrumentDetailsId.setNoMouvementFacture(noMouvementFacture);
            
        return this.repository.findById(reglementInstrumentDetailsId);
    }
    
    public ReglementInstrumentDetails save(ReglementInstrumentDetails reglementInstrumentDetails) {
        return this.repository.save(reglementInstrumentDetails);
    }
            
    public void delete(ReglementInstrumentDetails reglementInstrumentDetails) {
        this.repository.delete(reglementInstrumentDetails);
    }
}
