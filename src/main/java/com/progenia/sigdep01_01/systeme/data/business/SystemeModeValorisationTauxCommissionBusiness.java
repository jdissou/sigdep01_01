/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationTauxCommission;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeModeValorisationTauxCommissionRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationTauxCommissionBusiness {
    @Autowired
    private final SystemeModeValorisationTauxCommissionRepository repository;

    public SystemeModeValorisationTauxCommissionBusiness(SystemeModeValorisationTauxCommissionRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationTauxCommission> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationTauxCommission> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationTauxCommission save(SystemeModeValorisationTauxCommission modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationTauxCommission modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
