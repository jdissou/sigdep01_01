/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.Creancier;
import com.progenia.sigdep01_01.data.pojo.CreancierPojo;
import com.progenia.sigdep01_01.data.repository.CreancierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CreancierBusiness {
    @Autowired
    private final CreancierRepository repository;

    public CreancierBusiness(CreancierRepository repository) {
        this.repository = repository;
    }

    public Optional<Creancier> findById(String codeCreancier) {
        return this.repository.findById(codeCreancier);
    }

    public List<Creancier> findAll() {
        return this.repository.findAll();
    }

    public Creancier save(Creancier creancier) {
        return this.repository.save(creancier);
    }
            
    public void delete(Creancier creancier) {
        this.repository.delete(creancier);
    }
        
    public List<CreancierPojo> getReportData() {
        return this.repository.getReportData();
    }
}
