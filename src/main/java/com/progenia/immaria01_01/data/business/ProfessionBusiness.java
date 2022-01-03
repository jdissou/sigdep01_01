/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Profession;
import com.progenia.immaria01_01.data.repository.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ProfessionBusiness {
    @Autowired
    private final ProfessionRepository repository;

    public ProfessionBusiness(ProfessionRepository repository) {
        this.repository = repository;
    }

    public Optional<Profession> findById(String codeProfession) {
        return this.repository.findById(codeProfession);
    }

    public List<Profession> findAll() {
        return this.repository.findAll();
    }

    public Profession save(Profession Profession) {
        return this.repository.save(Profession);
    }
            
    public void delete(Profession Profession) {
        this.repository.delete(Profession);
    }
    
    public List<Profession> getReportData() {
        return this.repository.findAll();
    } 
    
}
