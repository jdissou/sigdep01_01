/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.TypePartenaire;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.TypePartenaireRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TypePartenaireBusiness {
    @Autowired
    private final TypePartenaireRepository repository;

    public TypePartenaireBusiness(TypePartenaireRepository repository) {
        this.repository = repository;
    }

    public Optional<TypePartenaire> findById(String codeTypePartenaire) {
        return this.repository.findById(codeTypePartenaire);
    }

    public List<TypePartenaire> findAll() {
        return this.repository.findAll();
    }

    public TypePartenaire save(TypePartenaire typePartenaire) {
        return this.repository.save(typePartenaire);
    }
            
    public void delete(TypePartenaire typePartenaire) {
        this.repository.delete(typePartenaire);
    }
    
    public List<TypePartenaire> getReportData() {
        return this.repository.findAll();
    } 
    
}
