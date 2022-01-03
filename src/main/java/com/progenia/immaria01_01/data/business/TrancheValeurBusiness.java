/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.pojo.TrancheValeurPojo;
import com.progenia.immaria01_01.data.entity.TrancheValeur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.TrancheValeurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TrancheValeurBusiness {
    @Autowired
    private final TrancheValeurRepository repository;

    public TrancheValeurBusiness(TrancheValeurRepository repository) {
        this.repository = repository;
    }

    public Optional<TrancheValeur> findById(String codeTrancheValeur) {
        return this.repository.findById(codeTrancheValeur);
    }

    public List<TrancheValeur> findAll() {
        return this.repository.findAll();
    }

    public TrancheValeur save(TrancheValeur compteTresorerie) {
        return this.repository.save(compteTresorerie);
    }
            
    public void delete(TrancheValeur compteTresorerie) {
        this.repository.delete(compteTresorerie);
    }

    public List<TrancheValeurPojo> getReportData() {
        return this.repository.getReportData();
    }    
}
