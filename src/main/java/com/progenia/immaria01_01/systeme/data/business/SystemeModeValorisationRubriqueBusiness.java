/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeModeValorisationRubrique;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeModeValorisationRubriqueRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationRubriqueBusiness {
    @Autowired
    private final SystemeModeValorisationRubriqueRepository repository;

    public SystemeModeValorisationRubriqueBusiness(SystemeModeValorisationRubriqueRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationRubrique> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationRubrique> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationRubrique save(SystemeModeValorisationRubrique modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationRubrique modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
