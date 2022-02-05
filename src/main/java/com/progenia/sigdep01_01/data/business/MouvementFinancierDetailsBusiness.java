/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.MouvementFinancierDetails;
import com.progenia.sigdep01_01.data.entity.MouvementFinancierDetailsId;
import com.progenia.sigdep01_01.data.repository.MouvementFinancierDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementFinancierDetailsBusiness {
    @Autowired
    private final MouvementFinancierDetailsRepository repository;

    public MouvementFinancierDetailsBusiness(MouvementFinancierDetailsRepository repository) {
        this.repository = repository;
    }

    public List<MouvementFinancierDetails> getDetailsRelatedDataByNoMouvement(Long noMouvement) {
        return this.repository.findByMouvementFinancierDetailsIdNoMouvement(noMouvement);
    }

    public List<MouvementFinancierDetails> getDetailsRelatedDataByNoEcheance(Integer noEcheance) {
        return this.repository.findByMouvementFinancierDetailsIdNoEcheance(noEcheance);
    }

    public List<MouvementFinancierDetails> getDetailsRelatedDataByCodeCommission(String codeCommission) {
        return this.repository.findByMouvementFinancierDetailsIdCodeCommission(codeCommission);
    }

    public List<MouvementFinancierDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<MouvementFinancierDetails> getMouvementFinancierDetails(Long noMouvement, Integer noEcheance, String codeCommission) {
        
        MouvementFinancierDetailsId mouvementFinancierDetailsId = new MouvementFinancierDetailsId();
        mouvementFinancierDetailsId.setNoMouvement(noMouvement);
        mouvementFinancierDetailsId.setNoEcheance(noEcheance);
        mouvementFinancierDetailsId.setCodeCommission(codeCommission);

        return this.repository.findById(mouvementFinancierDetailsId);
    }
    
    public MouvementFinancierDetails save(MouvementFinancierDetails mouvementFinancierDetails) {
        return this.repository.save(mouvementFinancierDetails);
    }
            
    public void delete(MouvementFinancierDetails mouvementFinancierDetails) {
        this.repository.delete(mouvementFinancierDetails);
    }
}
