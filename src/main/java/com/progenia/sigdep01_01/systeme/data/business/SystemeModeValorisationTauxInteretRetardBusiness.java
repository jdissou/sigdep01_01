/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationTauxInteretRetard;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeModeValorisationTauxInteretRetardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationTauxInteretRetardBusiness {
    @Autowired
    private final SystemeModeValorisationTauxInteretRetardRepository repository;

    public SystemeModeValorisationTauxInteretRetardBusiness(SystemeModeValorisationTauxInteretRetardRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationTauxInteretRetard> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationTauxInteretRetard> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationTauxInteretRetard save(SystemeModeValorisationTauxInteretRetard modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationTauxInteretRetard modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
