/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.pojo.ModeReglementPojo;
import com.progenia.sigdep01_01.data.entity.ModeReglement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.ModeReglementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ModeReglementBusiness {
    @Autowired
    private final ModeReglementRepository repository;

    public ModeReglementBusiness(ModeReglementRepository repository) {
        this.repository = repository;
    }

    public Optional<ModeReglement> findById(String codeModeReglement) {
        return this.repository.findById(codeModeReglement);
    }

    public List<ModeReglement> findAll() {
        return this.repository.findAll();
    }

    public ModeReglement save(ModeReglement modeReglement) {
        return this.repository.save(modeReglement);
    }
            
    public void delete(ModeReglement modeReglement) {
        this.repository.delete(modeReglement);
    }
        
        public List<ModeReglementPojo> getReportData() {
        return this.repository.getReportData();
    }
}
