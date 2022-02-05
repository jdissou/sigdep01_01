/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationCommission;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeModeValorisationCommissionRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationCommissionBusiness {
    @Autowired
    private final SystemeModeValorisationCommissionRepository repository;

    public SystemeModeValorisationCommissionBusiness(SystemeModeValorisationCommissionRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationCommission> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationCommission> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationCommission save(SystemeModeValorisationCommission modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationCommission modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
