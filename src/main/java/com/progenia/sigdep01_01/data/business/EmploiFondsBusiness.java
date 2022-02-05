/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.EmploiFonds;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.EmploiFondsRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class EmploiFondsBusiness {
    @Autowired
    private final EmploiFondsRepository repository;

    public EmploiFondsBusiness(EmploiFondsRepository repository) {
        this.repository = repository;
    }

    public Optional<EmploiFonds> findById(String codeEmploiFonds) {
        return this.repository.findById(codeEmploiFonds);
    }

    public List<EmploiFonds> findAll() {
        return this.repository.findAll();
    }

    public EmploiFonds save(EmploiFonds emploiFonds) {
        return this.repository.save(emploiFonds);
    }
            
    public void delete(EmploiFonds emploiFonds) {
        this.repository.delete(emploiFonds);
    }

    public List<EmploiFonds> getReportData() {
        return this.repository.findAll();
    }    
}
