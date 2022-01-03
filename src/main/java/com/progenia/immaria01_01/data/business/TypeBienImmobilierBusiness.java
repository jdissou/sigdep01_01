/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.TypeBienImmobilier;
import com.progenia.immaria01_01.data.repository.TypeBienImmobilierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypeBienImmobilierBusiness {
    @Autowired
    private final TypeBienImmobilierRepository repository;

    public TypeBienImmobilierBusiness(TypeBienImmobilierRepository repository) {
        this.repository = repository;
    }

    public Optional<TypeBienImmobilier> findById(String codeTypeBienImmobilier) {
        return this.repository.findById(codeTypeBienImmobilier);
    }

    public List<TypeBienImmobilier> findAll() {
        return this.repository.findAll();
    }

    public TypeBienImmobilier save(TypeBienImmobilier typeBienImmobilier) {
        return this.repository.save(typeBienImmobilier);
    }
            
    public void delete(TypeBienImmobilier typeBienImmobilier) {
        this.repository.delete(typeBienImmobilier);
    }
    
    public List<TypeBienImmobilier> getReportData() {
        return this.repository.findAll();
    } 
}
