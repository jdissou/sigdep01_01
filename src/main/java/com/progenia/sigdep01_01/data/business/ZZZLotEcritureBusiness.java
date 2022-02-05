/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.LotEcritureRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class LotEcritureBusiness {
    @Autowired
    private final LotEcritureRepository repository;

    public LotEcritureBusiness(LotEcritureRepository repository) {
        this.repository = repository;
    }

    public Optional<ZZZLotEcriture> findById(Long noBordereau) {
        return this.repository.findById(noBordereau);
    }

    public List<ZZZLotEcriture> findAll() {
        return this.repository.findAll();
    }

    public List<ZZZLotEcriture> findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(String codeValidation, String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(codeValidation, codeCentreIncubateur);
    }
            

    public ZZZLotEcriture save(ZZZLotEcriture mouvementCompta) {
        return this.repository.save(mouvementCompta);
    }
            
    public void delete(ZZZLotEcriture mouvementCompta) {
        this.repository.delete(mouvementCompta);
    }
    
    public List<ZZZLotEcriture> getExtraComptableData(String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur("E", codeCentreIncubateur);
    } 
}
