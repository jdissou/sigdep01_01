/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.pojo.TypePorteurPojo;
import com.progenia.immaria01_01.data.entity.TypePorteur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.TypePorteurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypePorteurBusiness {
    @Autowired
    private final TypePorteurRepository repository;

    public TypePorteurBusiness(TypePorteurRepository repository) {
        this.repository = repository;
    }

    public Optional<TypePorteur> findById(String codeTypePorteur) {
        return this.repository.findById(codeTypePorteur);
    }

    public List<TypePorteur> findAll() {
        return this.repository.findAll();
    }

    public TypePorteur save(TypePorteur typePorteur) {
        return this.repository.save(typePorteur);
    }
            
    public void delete(TypePorteur typePorteur) {
        this.repository.delete(typePorteur);
    }
        
        public List<TypePorteurPojo> getReportData() {
        return this.repository.getReportData();
    }
}
