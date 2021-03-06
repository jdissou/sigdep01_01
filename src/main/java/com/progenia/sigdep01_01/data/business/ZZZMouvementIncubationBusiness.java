/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.MouvementIncubation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.ZZZMouvementIncubationRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementIncubationBusiness {
    @Autowired
    private final ZZZMouvementIncubationRepository repository;

    public MouvementIncubationBusiness(ZZZMouvementIncubationRepository repository) {
        this.repository = repository;
    }

    public Optional<MouvementIncubation> findById(Long noMouvement) {
        return this.repository.findById(noMouvement);
    }

    public List<MouvementIncubation> findAll() {
        return this.repository.findAll();
    }

    public MouvementIncubation save(MouvementIncubation mouvementIncubation) {
        return this.repository.save(mouvementIncubation);
    }
            
    public void delete(MouvementIncubation mouvementIncubation) {
        this.repository.delete(mouvementIncubation);
    }
}
