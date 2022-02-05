/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ProfilFinancement;
import com.progenia.sigdep01_01.data.pojo.ProfilFinancementPojo;
import com.progenia.sigdep01_01.data.repository.ProfilFinancementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ProfilFinancementBusiness {
    @Autowired
    private final ProfilFinancementRepository repository;

    public ProfilFinancementBusiness(ProfilFinancementRepository repository) {
        this.repository = repository;
    }

    public Optional<ProfilFinancement> findById(String codeProfilFinancement) {
        return this.repository.findById(codeProfilFinancement);
    }

    public List<ProfilFinancement> findAll() {
        return this.repository.findAll();
    }

    public ProfilFinancement save(ProfilFinancement profilFinancement) {
        return this.repository.save(profilFinancement);
    }
            
    public void delete(ProfilFinancement profilFinancement) {
        this.repository.delete(profilFinancement);
    }
        
    public List<ProfilFinancementPojo> getReportData() {
        return this.repository.getReportData();
    }
}
