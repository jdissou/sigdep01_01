/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.PaiementCommissionDetails;
import com.progenia.sigdep01_01.data.entity.PaiementCommissionDetailsId;
import com.progenia.sigdep01_01.data.repository.PaiementCommissionDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PaiementCommissionDetailsBusiness {
    @Autowired
    private final PaiementCommissionDetailsRepository repository;

    public PaiementCommissionDetailsBusiness(PaiementCommissionDetailsRepository repository) {
        this.repository = repository;
    }

    public List<PaiementCommissionDetails> getDetailsRelatedDataByNoPaiement(Long noPaiement) {
        return this.repository.findByPaiementCommissionDetailsIdNoPaiement(noPaiement);
    }

    public List<PaiementCommissionDetails> getDetailsRelatedDataByCodeCommission(String codeCommission) {
        return this.repository.findByPaiementCommissionDetailsIdCodeCommission(codeCommission);
    }

    public List<PaiementCommissionDetails> getDetailsRelatedDataByNoEcheance(Integer noEcheance) {
        return this.repository.findByPaiementCommissionDetailsIdNoEcheance(noEcheance);
    }

    public List<PaiementCommissionDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<PaiementCommissionDetails> getPaiementCommissionDetails(Long noPaiement, String codeCommission, Integer noEcheance) {
        
        PaiementCommissionDetailsId paiementCommissionDetailsId = new PaiementCommissionDetailsId();
        paiementCommissionDetailsId.setNoPaiement(noPaiement);
        paiementCommissionDetailsId.setCodeCommission(codeCommission);
        paiementCommissionDetailsId.setNoEcheance(noEcheance);

        return this.repository.findById(paiementCommissionDetailsId);
    }
    
    public PaiementCommissionDetails save(PaiementCommissionDetails paiementCommissionDetails) {
        return this.repository.save(paiementCommissionDetails);
    }
            
    public void delete(PaiementCommissionDetails paiementCommissionDetails) {
        this.repository.delete(paiementCommissionDetails);
    }
}
