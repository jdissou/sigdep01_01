/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.pojo.IndicateurSuiviPojo;
import com.progenia.immaria01_01.data.entity.IndicateurSuivi;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.IndicateurSuiviRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class IndicateurSuiviBusiness {
    @Autowired
    private final IndicateurSuiviRepository repository;

    public IndicateurSuiviBusiness(IndicateurSuiviRepository repository) {
        this.repository = repository;
    }

    public Optional<IndicateurSuivi> findById(String codeIndicateur) {
        return this.repository.findById(codeIndicateur);
    }

    public List<IndicateurSuivi> findAll() {
        return this.repository.findAll();
    }

    public IndicateurSuivi save(IndicateurSuivi indicateurSuivi) {
        return this.repository.save(indicateurSuivi);
    }
            
    public void delete(IndicateurSuivi indicateurSuivi) {
        this.repository.delete(indicateurSuivi);
    }
    
    public List<IndicateurSuiviPojo> getReportData() {
        return this.repository.getReportData();
    }
}
