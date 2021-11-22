/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.PorteurPojo;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Cohorte;
import com.progenia.incubatis01_03.data.entity.Porteur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.PorteurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PorteurBusiness {
    @Autowired
    private final PorteurRepository repository;

    public PorteurBusiness(PorteurRepository repository) {
        this.repository = repository;
    }

    public Optional<Porteur> findById(String codeClient) {
        return this.repository.findById(codeClient);
    }

    public List<Porteur> findAll() {
        return this.repository.findAll();
    }

    public List<Porteur> findByCentreIncubateur(CentreIncubateur centreIncubateur) {
        return this.repository.findByCentreIncubateur(centreIncubateur);
    }

    public List<Porteur> findByCentreIncubateurAndCohorte(CentreIncubateur centreIncubateur, Cohorte cohorte) {
        return this.repository.findByCentreIncubateurAndCohorte(centreIncubateur, cohorte);
    }

    public Porteur save(Porteur client) {
        return this.repository.save(client);
    }
            
    public void delete(Porteur client) {
        this.repository.delete(client);
    }
    
    public List<PorteurPojo> getReportData() {
        return this.repository.getReportData();
    }
}
