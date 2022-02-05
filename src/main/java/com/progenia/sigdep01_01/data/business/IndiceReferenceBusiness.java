/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.IndiceReference;
import com.progenia.sigdep01_01.data.repository.IndiceReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class IndiceReferenceBusiness {
    @Autowired
    private final IndiceReferenceRepository repository;

    public IndiceReferenceBusiness(IndiceReferenceRepository repository) {
        this.repository = repository;
    }

    public Optional<IndiceReference> findById(String codeIndiceReference) {
        return this.repository.findById(codeIndiceReference);
    }

    public List<IndiceReference> findAll() {
        return this.repository.findAll();
    }

    public IndiceReference save(IndiceReference indiceReference) {
        return this.repository.save(indiceReference);
    }
            
    public void delete(IndiceReference indiceReference) {
        this.repository.delete(indiceReference);
    }

    public List<IndiceReference> getReportData() {
        return this.repository.findAll();
    }    
}
