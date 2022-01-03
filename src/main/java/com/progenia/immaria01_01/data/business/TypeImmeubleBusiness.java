/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.TypeImmeuble;
import com.progenia.immaria01_01.data.repository.TypeImmeubleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypeImmeubleBusiness {
    @Autowired
    private final TypeImmeubleRepository repository;

    public TypeImmeubleBusiness(TypeImmeubleRepository repository) {
        this.repository = repository;
    }

    public Optional<TypeImmeuble> findById(String codeTypeImmeuble) {
        return this.repository.findById(codeTypeImmeuble);
    }

    public List<TypeImmeuble> findAll() {
        return this.repository.findAll();
    }

    public TypeImmeuble save(TypeImmeuble typeImmeuble) {
        return this.repository.save(typeImmeuble);
    }
            
    public void delete(TypeImmeuble typeImmeuble) {
        this.repository.delete(typeImmeuble);
    }
    
    public List<TypeImmeuble> getReportData() {
        return this.repository.findAll();
    } 
}
