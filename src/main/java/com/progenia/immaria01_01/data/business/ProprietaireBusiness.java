/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Proprietaire;
import com.progenia.immaria01_01.data.pojo.ProprietairePojo;
import com.progenia.immaria01_01.data.repository.ProprietaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ProprietaireBusiness {
    @Autowired
    private final ProprietaireRepository repository;

    public ProprietaireBusiness(ProprietaireRepository repository) {
        this.repository = repository;
    }

    public Optional<Proprietaire> findById(String codeProprietaire) {
        return this.repository.findById(codeProprietaire);
    }

    public List<Proprietaire> findAll() {
        return this.repository.findAll();
    }

    public Proprietaire save(Proprietaire proprietaire) {
        return this.repository.save(proprietaire);
    }
            
    public void delete(Proprietaire proprietaire) {
        this.repository.delete(proprietaire);
    }
        
    public List<ProprietairePojo> getReportData() {
        return this.repository.getReportData();
    }
}
