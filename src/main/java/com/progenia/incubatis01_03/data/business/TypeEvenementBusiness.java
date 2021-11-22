/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.TypeEvenement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.TypeEvenementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypeEvenementBusiness {
    @Autowired
    private final TypeEvenementRepository repository;

    public TypeEvenementBusiness(TypeEvenementRepository repository) {
        this.repository = repository;
    }

    public Optional<TypeEvenement> findById(String codeTypeEvenement) {
        return this.repository.findById(codeTypeEvenement);
    }

    public List<TypeEvenement> findAll() {
        return this.repository.findAll();
    }

    public TypeEvenement save(TypeEvenement typeEvenement) {
        return this.repository.save(typeEvenement);
    }
            
    public void delete(TypeEvenement typeEvenement) {
        this.repository.delete(typeEvenement);
    }
    
    public List<TypeEvenement> getReportData() {
        return this.repository.findAll();
    }     
}
