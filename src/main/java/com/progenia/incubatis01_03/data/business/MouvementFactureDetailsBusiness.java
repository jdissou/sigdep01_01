/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.MouvementFactureDetails;
import com.progenia.incubatis01_03.data.entity.MouvementFactureDetailsId;
import com.progenia.incubatis01_03.data.repository.MouvementFactureDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementFactureDetailsBusiness {
    @Autowired
    private final MouvementFactureDetailsRepository repository;

    public MouvementFactureDetailsBusiness(MouvementFactureDetailsRepository repository) {
        this.repository = repository;
    }

    public List<MouvementFactureDetails> getDetailsRelatedDataByNoMouvement(Long noMouvement) {
        return this.repository.findByMouvementFactureDetailsIdNoMouvement(noMouvement);
    }
            
    public List<MouvementFactureDetails> getDetailsRelatedDataByCodeService(String codeService) {
        return this.repository.findByMouvementFactureDetailsIdCodeService(codeService);
    }
            
    public List<MouvementFactureDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<MouvementFactureDetails> findById(MouvementFactureDetailsId mouvementFactureDetailsId) {
        return this.repository.findById(mouvementFactureDetailsId);
    }

    public Optional<MouvementFactureDetails> getMouvementFactureDetails(Long noMouvement, String codeService) {
        
        MouvementFactureDetailsId mouvementFactureDetailsId = new MouvementFactureDetailsId();
        mouvementFactureDetailsId.setNoMouvement(noMouvement);
        mouvementFactureDetailsId.setCodeService(codeService);
            
        return this.repository.findById(mouvementFactureDetailsId);
    }
    
    public MouvementFactureDetails save(MouvementFactureDetails mouvementFactureDetails) {
        return this.repository.save(mouvementFactureDetails);
    }
            
    public void delete(MouvementFactureDetails mouvementFactureDetails) {
        this.repository.delete(mouvementFactureDetails);
    }
}
