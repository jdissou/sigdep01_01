/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.NatureIndicateur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.NatureIndicateurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class NatureIndicateurBusiness {
    @Autowired
    private final NatureIndicateurRepository repository;

    public NatureIndicateurBusiness(NatureIndicateurRepository repository) {
        this.repository = repository;
    }

    public Optional<NatureIndicateur> findById(String codeNatureIndicateur) {
        return this.repository.findById(codeNatureIndicateur);
    }

    public List<NatureIndicateur> findAll() {
        return this.repository.findAll();
    }

    public NatureIndicateur save(NatureIndicateur natureIndicateur) {
        return this.repository.save(natureIndicateur);
    }
            
    public void delete(NatureIndicateur natureIndicateur) {
        this.repository.delete(natureIndicateur);
    }
    
    public List<NatureIndicateur> getReportData() {
        return this.repository.findAll();
    } 
}
