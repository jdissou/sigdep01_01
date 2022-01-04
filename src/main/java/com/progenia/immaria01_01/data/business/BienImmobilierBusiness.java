/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.BienImmobilier;
import com.progenia.immaria01_01.data.pojo.BienImmobilierPojo;
import com.progenia.immaria01_01.data.repository.BienImmobilierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class BienImmobilierBusiness {
    @Autowired
    private final BienImmobilierRepository repository;

    public BienImmobilierBusiness(BienImmobilierRepository repository) {
        this.repository = repository;
    }

    public Optional<BienImmobilier> findById(String codeBienImmobilier) {
        return this.repository.findById(codeBienImmobilier);
    }

    public List<BienImmobilier> findAll() {
        return this.repository.findAll();
    }

    public BienImmobilier save(BienImmobilier bienImmobilier) {
        return this.repository.save(bienImmobilier);
    }
            
    public void delete(BienImmobilier bienImmobilier) {
        this.repository.delete(bienImmobilier);
    }
        
    public List<BienImmobilierPojo> getReportData() {
        return this.repository.getReportData();
    }
}
