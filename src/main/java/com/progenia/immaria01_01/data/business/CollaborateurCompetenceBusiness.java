/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CollaborateurCompetence;
import com.progenia.immaria01_01.data.entity.CollaborateurCompetenceId;
import com.progenia.immaria01_01.data.repository.CollaborateurCompetenceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CollaborateurCompetenceBusiness {
    @Autowired
    private final CollaborateurCompetenceRepository repository;

    public CollaborateurCompetenceBusiness(CollaborateurCompetenceRepository repository) {
        this.repository = repository;
    }

    public List<CollaborateurCompetence> getRelatedDataByCodeCollaborateur(String codeCollaborateur) {
        return this.repository.findByCollaborateurCompetenceIdCodeCollaborateur(codeCollaborateur);
    }
            
    public List<CollaborateurCompetence> getRelatedDataByCodeCompetence(String codeCompetence) {
        return this.repository.findByCollaborateurCompetenceIdCodeCompetence(codeCompetence);
    }
            
    public List<CollaborateurCompetence> findAll() {
        return this.repository.findAll();
    }

    public Optional<CollaborateurCompetence> findById(CollaborateurCompetenceId collaborateurCompetenceId) {
        return this.repository.findById(collaborateurCompetenceId);
    }

    public Optional<CollaborateurCompetence> getCollaborateurCompetence(String codeCollaborateur, String codeCompetence) {
        
        CollaborateurCompetenceId collaborateurCompetenceId = new CollaborateurCompetenceId();
        collaborateurCompetenceId.setCodeCollaborateur(codeCollaborateur);
        collaborateurCompetenceId.setCodeCompetence(codeCompetence);
            
        return this.repository.findById(collaborateurCompetenceId);
    }
    
    public CollaborateurCompetence save(CollaborateurCompetence collaborateurCompetence) {
        return this.repository.save(collaborateurCompetence);
    }
            
    public void delete(CollaborateurCompetence collaborateurCompetence) {
        this.repository.delete(collaborateurCompetence);
    }
}
