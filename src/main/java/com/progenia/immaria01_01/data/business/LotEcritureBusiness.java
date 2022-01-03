/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.LotEcriture;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.LotEcritureRepository;

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

    public Optional<LotEcriture> findById(Long noBordereau) {
        return this.repository.findById(noBordereau);
    }

    public List<LotEcriture> findAll() {
        return this.repository.findAll();
    }

    public List<LotEcriture> findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(String codeValidation, String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(codeValidation, codeCentreIncubateur);
    }
            

    public LotEcriture save(LotEcriture mouvementCompta) {
        return this.repository.save(mouvementCompta);
    }
            
    public void delete(LotEcriture mouvementCompta) {
        this.repository.delete(mouvementCompta);
    }
    
    public List<LotEcriture> getExtraComptableData(String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur("E", codeCentreIncubateur);
    } 
}
