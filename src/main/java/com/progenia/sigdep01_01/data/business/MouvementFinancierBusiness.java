/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.MouvementFinancier;
import com.progenia.sigdep01_01.data.repository.MouvementFinancierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementFinancierBusiness {
    @Autowired
    private final MouvementFinancierRepository repository;

    public MouvementFinancierBusiness(MouvementFinancierRepository repository) {
        this.repository = repository;
    }

    public Optional<MouvementFinancier> findById(Long noMouvement) {
        return this.repository.findById(noMouvement);
    }

    public List<MouvementFinancier> findAll() {
        return this.repository.findAll();
    }

    public MouvementFinancier save(MouvementFinancier mouvementFinancier) {
        return this.repository.save(mouvementFinancier);
    }
            
    public void delete(MouvementFinancier mouvementFinancier) {
        this.repository.delete(mouvementFinancier);
    }
}
