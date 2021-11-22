/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.Competence;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.CompetenceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CompetenceBusiness {
    @Autowired
    private final CompetenceRepository repository;

    public CompetenceBusiness(CompetenceRepository repository) {
        this.repository = repository;
    }

    public Optional<Competence> findById(String codeCompetence) {
        return this.repository.findById(codeCompetence);
    }

    public List<Competence> findAll() {
        return this.repository.findAll();
    }

    public Competence save(Competence competence) {
        return this.repository.save(competence);
    }
            
    public void delete(Competence competence) {
        this.repository.delete(competence);
    }
    
    public List<Competence> getReportData() {
        return this.repository.findAll();
    } 
}
