/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.CentreIncubateurPojo;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.CentreIncubateurRepository;

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

    public Optional<CentreIncubateur> findById(String codeCentreIncubateur) {
        return this.repository.findById(codeCentreIncubateur);
    }

    public List<CentreIncubateur> findAll() {
        return this.repository.findAll();
    }

    public CentreIncubateur save(CentreIncubateur centreIncubateur) {
        return this.repository.save(centreIncubateur);
    }
            
    public void delete(CentreIncubateur centreIncubateur) {
        this.repository.delete(centreIncubateur);
    }
    
    public List<CentreIncubateurPojo> getReportData() {
        return this.repository.getReportData();
    }

}
