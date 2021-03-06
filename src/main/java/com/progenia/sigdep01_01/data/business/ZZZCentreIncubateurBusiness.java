/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.pojo.CentreIncubateurPojo;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.CentreIncubateurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CentreIncubateurBusiness {
    @Autowired
    private final CentreIncubateurRepository repository;

    public CentreIncubateurBusiness(CentreIncubateurRepository repository) {
        this.repository = repository;
    }

    public Optional<ZZZCentreIncubateur> findById(String codeCentreIncubateur) {
        return this.repository.findById(codeCentreIncubateur);
    }

    public List<ZZZCentreIncubateur> findAll() {
        return this.repository.findAll();
    }

    public ZZZCentreIncubateur save(ZZZCentreIncubateur centreIncubateur) {
        return this.repository.save(centreIncubateur);
    }
            
    public void delete(ZZZCentreIncubateur centreIncubateur) {
        this.repository.delete(centreIncubateur);
    }
    
    public List<CentreIncubateurPojo> getReportData() {
        return this.repository.getReportData();
    }

}
