/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.TypeCollaborateur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.TypeCollaborateurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypeCollaborateurBusiness {
    @Autowired
    private final TypeCollaborateurRepository repository;

    public TypeCollaborateurBusiness(TypeCollaborateurRepository repository) {
        this.repository = repository;
    }

    public Optional<TypeCollaborateur> findById(String codeTypeCollaborateur) {
        return this.repository.findById(codeTypeCollaborateur);
    }

    public List<TypeCollaborateur> findAll() {
        return this.repository.findAll();
    }

    public TypeCollaborateur save(TypeCollaborateur typeCollaborateur) {
        return this.repository.save(typeCollaborateur);
    }
            
    public void delete(TypeCollaborateur typeCollaborateur) {
        this.repository.delete(typeCollaborateur);
    }
    
    public List<TypeCollaborateur> getReportData() {
        return this.repository.findAll();
    }  
}
