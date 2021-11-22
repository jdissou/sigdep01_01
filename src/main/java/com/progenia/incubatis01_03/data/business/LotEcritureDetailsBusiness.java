/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.LotEcritureDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.LotEcritureDetailsRepository;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class LotEcritureDetailsBusiness {
    @Autowired
    private final LotEcritureDetailsRepository repository;

    public LotEcritureDetailsBusiness(LotEcritureDetailsRepository repository) {
        this.repository = repository;
    }

    public Optional<LotEcritureDetails> findById(Long noMouvement) {
        return this.repository.findById(noMouvement);
    }

    public List<LotEcritureDetails> getDetailsRelatedDataByNoBordereau(Long noBordereau) {
        return this.repository.findByLotEcritureNoBordereau(noBordereau);
    }
            
    public List<LotEcritureDetails> findAll() {
        return this.repository.findAll();
    }

    public LotEcritureDetails save(LotEcritureDetails tableauCollecteDetails) {
        return this.repository.save(tableauCollecteDetails);
    }
            
    public void delete(LotEcritureDetails tableauCollecteDetails) {
        this.repository.delete(tableauCollecteDetails);
    }
}
