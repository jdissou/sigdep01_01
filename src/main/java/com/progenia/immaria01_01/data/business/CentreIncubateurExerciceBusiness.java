/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CentreIncubateurExercice;
import com.progenia.immaria01_01.data.entity.CentreIncubateurExerciceId;
import com.progenia.immaria01_01.data.repository.CentreIncubateurExerciceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CentreIncubateurExerciceBusiness {
    @Autowired
    private final CentreIncubateurExerciceRepository repository;

    public CentreIncubateurExerciceBusiness(CentreIncubateurExerciceRepository repository) {
        this.repository = repository;
    }

    public List<CentreIncubateurExercice> getRelatedDataByCodeCentreIncubateur(String codeCentreIncubateur) {
        return this.repository.findByCentreIncubateurExerciceIdCodeCentreIncubateur(codeCentreIncubateur);
    }
            
    public List<CentreIncubateurExercice> getRelatedDataByNoExercice(Integer noExercice) {
        return this.repository.findByCentreIncubateurExerciceIdNoExercice(noExercice);
    }
            
    public List<CentreIncubateurExercice> findAll() {
        return this.repository.findAll();
    }

    public Optional<CentreIncubateurExercice> findById(CentreIncubateurExerciceId centreIncubateurExerciceId) {
        return this.repository.findById(centreIncubateurExerciceId);
    }

    public Optional<CentreIncubateurExercice> getCentreIncubateurExercice(String codeCentreIncubateur, Integer noExercice) {
        
        CentreIncubateurExerciceId centreIncubateurExerciceId = new CentreIncubateurExerciceId();
        centreIncubateurExerciceId.setCodeCentreIncubateur(codeCentreIncubateur);
        centreIncubateurExerciceId.setNoExercice(noExercice);
            
        return this.repository.findById(centreIncubateurExerciceId);
    }
    
    public CentreIncubateurExercice save(CentreIncubateurExercice centreIncubateurExercice) {
        return this.repository.save(centreIncubateurExercice);
    }
            
    public void delete(CentreIncubateurExercice centreIncubateurExercice) {
        this.repository.delete(centreIncubateurExercice);
    }
}
