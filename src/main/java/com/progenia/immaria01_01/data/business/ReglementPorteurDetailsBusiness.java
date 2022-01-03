/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.ReglementPorteurDetails;
import com.progenia.immaria01_01.data.entity.ReglementPorteurDetailsId;
import com.progenia.immaria01_01.data.repository.ReglementPorteurDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ReglementPorteurDetailsBusiness {
    @Autowired
    private final ReglementPorteurDetailsRepository repository;

    public ReglementPorteurDetailsBusiness(ReglementPorteurDetailsRepository repository) {
        this.repository = repository;
    }

    public List<ReglementPorteurDetails> getDetailsRelatedDataByNoReglement(Long noReglement) {
        return this.repository.findByReglementPorteurDetailsIdNoReglement(noReglement);
    }
            
    public List<ReglementPorteurDetails> getDetailsRelatedDataByNoMouvementFacture(Long noMouvementFacture) {
        return this.repository.findByReglementPorteurDetailsIdNoMouvementFacture(noMouvementFacture);
    }
            
    public List<ReglementPorteurDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ReglementPorteurDetails> findById(ReglementPorteurDetailsId reglementPorteurDetailsId) {
        return this.repository.findById(reglementPorteurDetailsId);
    }

    public Optional<ReglementPorteurDetails> getReglementPorteurDetails(Long noReglement, Long noMouvementFacture) {
        
        ReglementPorteurDetailsId reglementPorteurDetailsId = new ReglementPorteurDetailsId();
        reglementPorteurDetailsId.setNoReglement(noReglement);
        reglementPorteurDetailsId.setNoMouvementFacture(noMouvementFacture);
            
        return this.repository.findById(reglementPorteurDetailsId);
    }
    
    public ReglementPorteurDetails save(ReglementPorteurDetails reglementPorteurDetails) {
        return this.repository.save(reglementPorteurDetails);
    }
            
    public void delete(ReglementPorteurDetails reglementPorteurDetails) {
        this.repository.delete(reglementPorteurDetails);
    }
}
