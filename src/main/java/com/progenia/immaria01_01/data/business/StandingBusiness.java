/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Standing;
import com.progenia.immaria01_01.data.repository.StandingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class StandingBusiness {
    @Autowired
    private final StandingRepository repository;

    public StandingBusiness(StandingRepository repository) {
        this.repository = repository;
    }

    public Optional<Standing> findById(String codeStanding) {
        return this.repository.findById(codeStanding);
    }

    public List<Standing> findAll() {
        return this.repository.findAll();
    }

    public Standing save(Standing standing) {
        return this.repository.save(standing);
    }
            
    public void delete(Standing standing) {
        this.repository.delete(standing);
    }
    
    public List<Standing> getReportData() {
        return this.repository.findAll();
    } 
}
