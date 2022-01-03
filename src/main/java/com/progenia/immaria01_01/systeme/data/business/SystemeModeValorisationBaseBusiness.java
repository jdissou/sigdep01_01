/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeModeValorisationBase;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeModeValorisationBaseRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationBaseBusiness {
    @Autowired
    private final SystemeModeValorisationBaseRepository repository;

    public SystemeModeValorisationBaseBusiness(SystemeModeValorisationBaseRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationBase> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationBase> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationBase save(SystemeModeValorisationBase modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationBase modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
