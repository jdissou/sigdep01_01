/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationBaseCommission;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeModeValorisationBaseCommissionRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationBaseCommissionBusiness {
    @Autowired
    private final SystemeModeValorisationBaseCommissionRepository repository;

    public SystemeModeValorisationBaseCommissionBusiness(SystemeModeValorisationBaseCommissionRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationBaseCommission> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationBaseCommission> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationBaseCommission save(SystemeModeValorisationBaseCommission modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationBaseCommission modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
