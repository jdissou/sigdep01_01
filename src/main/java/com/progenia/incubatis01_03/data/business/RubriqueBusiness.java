/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.RubriquePojo;
import com.progenia.incubatis01_03.data.entity.Rubrique;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.RubriqueRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class RubriqueBusiness {
    @Autowired
    private final RubriqueRepository repository;

    public RubriqueBusiness(RubriqueRepository repository) {
        this.repository = repository;
    }

    public Optional<Rubrique> findById(String codeClient) {
        return this.repository.findById(codeClient);
    }

    public List<Rubrique> findAll() {
        return this.repository.findAll();
    }

    public Rubrique save(Rubrique client) {
        return this.repository.save(client);
    }
            
    public void delete(Rubrique client) {
        this.repository.delete(client);
    }
    
    public List<RubriquePojo> getReportData() {
        return this.repository.getReportData();
    }
}
