/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Nationalite;
import com.progenia.immaria01_01.data.repository.NationaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class NationaliteBusiness {
    @Autowired
    private final NationaliteRepository repository;

    public NationaliteBusiness(NationaliteRepository repository) {
        this.repository = repository;
    }

    public Optional<Nationalite> findById(String codeNationalite) {
        return this.repository.findById(codeNationalite);
    }

    public List<Nationalite> findAll() {
        return this.repository.findAll();
    }

    public Nationalite save(Nationalite Nationalite) {
        return this.repository.save(Nationalite);
    }
            
    public void delete(Nationalite Nationalite) {
        this.repository.delete(Nationalite);
    }
    
    public List<Nationalite> getReportData() {
        return this.repository.findAll();
    } 
    
}
