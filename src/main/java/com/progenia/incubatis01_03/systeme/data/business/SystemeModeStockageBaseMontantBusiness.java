/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.business;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeModeStockageBaseMontant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.systeme.data.repository.SystemeModeStockageBaseMontantRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeStockageBaseMontantBusiness {
    @Autowired
    private final SystemeModeStockageBaseMontantRepository repository;

    public SystemeModeStockageBaseMontantBusiness(SystemeModeStockageBaseMontantRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeStockageBaseMontant> findById(String codeModeStockage) {
        return this.repository.findById(codeModeStockage);
    }

    public List<SystemeModeStockageBaseMontant> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeStockageBaseMontant save(SystemeModeStockageBaseMontant modeStockage) {
        return this.repository.save(modeStockage);
    }
            
    public void delete(SystemeModeStockageBaseMontant modeStockage) {
        this.repository.delete(modeStockage);
    } 
}
