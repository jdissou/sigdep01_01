/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.RubriqueComptabilisation;
import com.progenia.incubatis01_03.data.entity.RubriqueComptabilisationId;
import com.progenia.incubatis01_03.data.repository.RubriqueComptabilisationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class RubriqueComptabilisationBusiness {
    @Autowired
    private final RubriqueComptabilisationRepository repository;

    public RubriqueComptabilisationBusiness(RubriqueComptabilisationRepository repository) {
        this.repository = repository;
    }

    public List<RubriqueComptabilisation> getRelatedDataByNoRubrique(String noRubrique) {
        return this.repository.findByRubriqueComptabilisationIdNoRubrique(noRubrique);
    }
            
    public List<RubriqueComptabilisation> getRelatedDataByCodeTypePorteur(String codeTypePorteur) {
        return this.repository.findByRubriqueComptabilisationIdCodeTypePorteur(codeTypePorteur);
    }
            
    public List<RubriqueComptabilisation> findAll() {
        return this.repository.findAll();
    }

    public Optional<RubriqueComptabilisation> getRubriqueComptabilisation(String noRubrique, String codeTypePorteur) {
        
        RubriqueComptabilisationId rubriqueComptabilisationId = new RubriqueComptabilisationId();
        rubriqueComptabilisationId.setNoRubrique(noRubrique);
        rubriqueComptabilisationId.setCodeTypePorteur(codeTypePorteur);
            
        return this.repository.findById(rubriqueComptabilisationId);
    }
    
    public RubriqueComptabilisation save(RubriqueComptabilisation rubriqueComptabilisation) {
        return this.repository.save(rubriqueComptabilisation);
    }
            
    public void delete(RubriqueComptabilisation rubriqueComptabilisation) {
        this.repository.delete(rubriqueComptabilisation);
    }
}
