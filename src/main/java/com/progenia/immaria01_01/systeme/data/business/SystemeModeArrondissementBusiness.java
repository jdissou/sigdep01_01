/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeModeArrondissement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeModeArrondissementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeArrondissementBusiness {
    @Autowired
    private final SystemeModeArrondissementRepository repository;

    public SystemeModeArrondissementBusiness(SystemeModeArrondissementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeArrondissement> findById(String codeModeArrondissement) {
        return this.repository.findById(codeModeArrondissement);
    }

    public List<SystemeModeArrondissement> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeArrondissement save(SystemeModeArrondissement modeArrondissement) {
        return this.repository.save(modeArrondissement);
    }
            
    public void delete(SystemeModeArrondissement modeArrondissement) {
        this.repository.delete(modeArrondissement);
    } 
}
