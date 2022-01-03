/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeModeValorisationTaux;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeModeValorisationTauxRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeValorisationTauxBusiness {
    @Autowired
    private final SystemeModeValorisationTauxRepository repository;

    public SystemeModeValorisationTauxBusiness(SystemeModeValorisationTauxRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeValorisationTaux> findById(String codeModeValorisation) {
        return this.repository.findById(codeModeValorisation);
    }

    public List<SystemeModeValorisationTaux> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeValorisationTaux save(SystemeModeValorisationTaux modeValorisation) {
        return this.repository.save(modeValorisation);
    }
            
    public void delete(SystemeModeValorisationTaux modeValorisation) {
        this.repository.delete(modeValorisation);
    } 
}
