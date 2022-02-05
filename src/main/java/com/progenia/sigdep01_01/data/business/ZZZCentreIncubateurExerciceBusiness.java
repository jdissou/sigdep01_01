/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateurExercice;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateurExerciceId;
import com.progenia.sigdep01_01.data.repository.CentreIncubateurExerciceRepository;
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

    public List<ZZZCentreIncubateurExercice> getRelatedDataByCodeCentreIncubateur(String codeCentreIncubateur) {
        return this.repository.findByCentreIncubateurExerciceIdCodeCentreIncubateur(codeCentreIncubateur);
    }
            
    public List<ZZZCentreIncubateurExercice> getRelatedDataByNoExercice(Integer noExercice) {
        return this.repository.findByCentreIncubateurExerciceIdNoExercice(noExercice);
    }
            
    public List<ZZZCentreIncubateurExercice> findAll() {
        return this.repository.findAll();
    }

    public Optional<ZZZCentreIncubateurExercice> findById(ZZZCentreIncubateurExerciceId centreIncubateurExerciceId) {
        return this.repository.findById(centreIncubateurExerciceId);
    }

    public Optional<ZZZCentreIncubateurExercice> getCentreIncubateurExercice(String codeCentreIncubateur, Integer noExercice) {
        
        ZZZCentreIncubateurExerciceId centreIncubateurExerciceId = new ZZZCentreIncubateurExerciceId();
        centreIncubateurExerciceId.setCodeCentreIncubateur(codeCentreIncubateur);
        centreIncubateurExerciceId.setNoExercice(noExercice);
            
        return this.repository.findById(centreIncubateurExerciceId);
    }
    
    public ZZZCentreIncubateurExercice save(ZZZCentreIncubateurExercice centreIncubateurExercice) {
        return this.repository.save(centreIncubateurExercice);
    }
            
    public void delete(ZZZCentreIncubateurExercice centreIncubateurExercice) {
        this.repository.delete(centreIncubateurExercice);
    }
}
