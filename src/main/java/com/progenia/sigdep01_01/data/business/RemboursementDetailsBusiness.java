/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.RemboursementDetails;
import com.progenia.sigdep01_01.data.entity.RemboursementDetailsId;
import com.progenia.sigdep01_01.data.repository.RemboursementDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class RemboursementDetailsBusiness {
    @Autowired
    private final RemboursementDetailsRepository repository;

    public RemboursementDetailsBusiness(RemboursementDetailsRepository repository) {
        this.repository = repository;
    }

    public List<RemboursementDetails> getDetailsRelatedDataByNoRemboursement(Long noRemboursement) {
        return this.repository.findByRemboursementDetailsIdNoRemboursement(noRemboursement);
    }

    public List<RemboursementDetails> getDetailsRelatedDataByNoEcheance(Integer noEcheance) {
        return this.repository.findByRemboursementDetailsIdNoEcheance(noEcheance);
    }

    /*
    public List<RemboursementDetails> getDetailsRelatedDataByCodeCommission(String codeCommission) {
        return this.repository.findByRemboursementDetailsIdCodeCommission(codeCommission);
    }
     */
            
    public List<RemboursementDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<RemboursementDetails> getRemboursementDetails(Long noRemboursement, Integer noEcheance) { //getRemboursementDetails(Long noRemboursement, String codeCommission, Integer noEcheance) {
        
        RemboursementDetailsId remboursementDetailsId = new RemboursementDetailsId();
        remboursementDetailsId.setNoRemboursement(noRemboursement);
        /* remboursementDetailsId.setCodeCommission(codeCommission); */
        remboursementDetailsId.setNoEcheance(noEcheance);

        return this.repository.findById(remboursementDetailsId);
    }
    
    public RemboursementDetails save(RemboursementDetails remboursementDetails) {
        return this.repository.save(remboursementDetails);
    }
            
    public void delete(RemboursementDetails remboursementDetails) {
        this.repository.delete(remboursementDetails);
    }
}
