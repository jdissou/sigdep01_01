/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemePeriodeFixationTaux;
import com.progenia.sigdep01_01.systeme.data.repository.SystemePeriodeFixationTauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemePeriodeFixationTauxBusiness {
    @Autowired
    private final SystemePeriodeFixationTauxRepository repository;

    public SystemePeriodeFixationTauxBusiness(SystemePeriodeFixationTauxRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemePeriodeFixationTaux> findById(String codePeriode) {
        return this.repository.findById(codePeriode);
    }

    public List<SystemePeriodeFixationTaux> findAll() {
        return this.repository.findAll();
    }

    public SystemePeriodeFixationTaux save(SystemePeriodeFixationTaux periodeFixationTaux) {
        return this.repository.save(periodeFixationTaux);
    }
            
    public void delete(SystemePeriodeFixationTaux periodeFixationTaux) {
        this.repository.delete(periodeFixationTaux);
    } 
}
