/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.BureauRegional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.BureauRegionalRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class BureauRegionalBusiness {
    @Autowired
    private final BureauRegionalRepository repository;

    public BureauRegionalBusiness(BureauRegionalRepository repository) {
        this.repository = repository;
    }

    public Optional<BureauRegional> findById(String codeBureauRegional) {
        return this.repository.findById(codeBureauRegional);
    }

    public List<BureauRegional> findAll() {
        return this.repository.findAll();
    }

    public BureauRegional save(BureauRegional bureauRegional) {
        return this.repository.save(bureauRegional);
    }
            
    public void delete(BureauRegional bureauRegional) {
        this.repository.delete(bureauRegional);
    }
    
    public List<BureauRegional> getReportData() {
        return this.repository.findAll();
    } 
}
