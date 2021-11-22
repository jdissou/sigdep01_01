/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.business;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeModeAbattement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.systeme.data.repository.SystemeModeAbattementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeAbattementBusiness {
    @Autowired
    private final SystemeModeAbattementRepository repository;

    public SystemeModeAbattementBusiness(SystemeModeAbattementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeAbattement> findById(String codeModeAbattement) {
        return this.repository.findById(codeModeAbattement);
    }

    public List<SystemeModeAbattement> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeAbattement save(SystemeModeAbattement modeAbattement) {
        return this.repository.save(modeAbattement);
    }
            
    public void delete(SystemeModeAbattement modeAbattement) {
        this.repository.delete(modeAbattement);
    } 
}
