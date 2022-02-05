/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.Monnaie;
import com.progenia.sigdep01_01.data.repository.MonnaieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MonnaieBusiness {
    @Autowired
    private final MonnaieRepository repository;

    public MonnaieBusiness(MonnaieRepository repository) {
        this.repository = repository;
    }

    public Optional<Monnaie> findById(String codeNationalite) {
        return this.repository.findById(codeNationalite);
    }

    public List<Monnaie> findAll() {
        return this.repository.findAll();
    }

    public Monnaie save(Monnaie monnaie) {
        return this.repository.save(monnaie);
    }
            
    public void delete(Monnaie monnaie) {
        this.repository.delete(monnaie);
    }
    
    public List<Monnaie> getReportData() {
        return this.repository.findAll();
    } 
    
}
