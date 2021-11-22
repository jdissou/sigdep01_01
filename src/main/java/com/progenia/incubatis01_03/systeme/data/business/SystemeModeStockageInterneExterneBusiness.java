/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.business;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeModeStockageInterneExterne;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.systeme.data.repository.SystemeModeStockageInterneExterneRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeModeStockageInterneExterneBusiness {
    @Autowired
    private final SystemeModeStockageInterneExterneRepository repository;

    public SystemeModeStockageInterneExterneBusiness(SystemeModeStockageInterneExterneRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeModeStockageInterneExterne> findById(String codeModeStockage) {
        return this.repository.findById(codeModeStockage);
    }

    public List<SystemeModeStockageInterneExterne> findAll() {
        return this.repository.findAll();
    }

    public SystemeModeStockageInterneExterne save(SystemeModeStockageInterneExterne modeStockage) {
        return this.repository.save(modeStockage);
    }
            
    public void delete(SystemeModeStockageInterneExterne modeStockage) {
        this.repository.delete(modeStockage);
    } 
}
