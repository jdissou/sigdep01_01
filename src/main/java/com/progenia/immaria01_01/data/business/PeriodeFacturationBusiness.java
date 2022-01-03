/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.PeriodeFacturation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.PeriodeFacturationRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PeriodeFacturationBusiness {
    @Autowired
    private final PeriodeFacturationRepository repository;

    public PeriodeFacturationBusiness(PeriodeFacturationRepository repository) {
        this.repository = repository;
    }

    public Optional<PeriodeFacturation> findById(Integer noPeriode) {
        return this.repository.findById(noPeriode);
    }

    public List<PeriodeFacturation> findAll() {
        return this.repository.findAll();
    }

    public PeriodeFacturation save(PeriodeFacturation periodeFacturation) {
        return this.repository.save(periodeFacturation);
    }
            
    public void delete(PeriodeFacturation periodeFacturation) {
        this.repository.delete(periodeFacturation);
    }
    
    public List<PeriodeFacturation> getReportData() {
        return this.repository.findAll();
    } 
}
