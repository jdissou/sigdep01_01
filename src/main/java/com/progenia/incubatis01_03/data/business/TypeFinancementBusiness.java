/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.TypeFinancement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.TypeFinancementRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypeFinancementBusiness {
    @Autowired
    private final TypeFinancementRepository repository;

    public TypeFinancementBusiness(TypeFinancementRepository repository) {
        this.repository = repository;
    }

    public Optional<TypeFinancement> findById(String codeTypeFinancement) {
        return this.repository.findById(codeTypeFinancement);
    }

    public List<TypeFinancement> findAll() {
        return this.repository.findAll();
    }

    public TypeFinancement save(TypeFinancement typeFinancement) {
        return this.repository.save(typeFinancement);
    }
            
    public void delete(TypeFinancement typeFinancement) {
        this.repository.delete(typeFinancement);
    }
    
    public List<TypeFinancement> getReportData() {
        return this.repository.findAll();
    } 
}
