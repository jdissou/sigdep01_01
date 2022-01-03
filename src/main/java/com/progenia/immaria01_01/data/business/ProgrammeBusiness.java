/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Programme;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ProgrammeRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ProgrammeBusiness {
    @Autowired
    private final ProgrammeRepository repository;

    public ProgrammeBusiness(ProgrammeRepository repository) {
        this.repository = repository;
    }

    public Optional<Programme> findById(String codeProgramme) {
        return this.repository.findById(codeProgramme);
    }

    public List<Programme> findAll() {
        return this.repository.findAll();
    }

    public Programme save(Programme programme) {
        return this.repository.save(programme);
    }
            
    public void delete(Programme programme) {
        this.repository.delete(programme);
    }
    
    public List<Programme> getReportData() {
        return this.repository.findAll();
    }     
}
