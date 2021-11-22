/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.business;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeCategoriePorteur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.systeme.data.repository.SystemeCategoriePorteurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeCategoriePorteurBusiness {
    @Autowired
    private final SystemeCategoriePorteurRepository repository;

    public SystemeCategoriePorteurBusiness(SystemeCategoriePorteurRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeCategoriePorteur> findById(String codeCategoriePorteur) {
        return this.repository.findById(codeCategoriePorteur);
    }

    public List<SystemeCategoriePorteur> findAll() {
        return this.repository.findAll();
    }

    public SystemeCategoriePorteur save(SystemeCategoriePorteur categoriePorteur) {
        return this.repository.save(categoriePorteur);
    }
            
    public void delete(SystemeCategoriePorteur categoriePorteur) {
        this.repository.delete(categoriePorteur);
    } 
}
