/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.CollaborateurPojo;
import com.progenia.incubatis01_03.data.entity.Collaborateur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.CollaborateurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CollaborateurBusiness {
    @Autowired
    private final CollaborateurRepository repository;

    public CollaborateurBusiness(CollaborateurRepository repository) {
        this.repository = repository;
    }

    public Optional<Collaborateur> findById(String codeCollaborateur) {
        return this.repository.findById(codeCollaborateur);
    }

    public List<Collaborateur> findAll() {
        return this.repository.findAll();
    }

    public Collaborateur save(Collaborateur collaborateur) {
        return this.repository.save(collaborateur);
    }
            
    public void delete(Collaborateur collaborateur) {
        this.repository.delete(collaborateur);
    }
    
    public List<CollaborateurPojo> getReportData() {
        return this.repository.getReportData();
    }
}
