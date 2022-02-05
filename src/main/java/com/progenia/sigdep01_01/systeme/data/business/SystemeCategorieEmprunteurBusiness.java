/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeCategorieEmprunteur;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeCategorieEmprunteurRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeCategorieEmprunteurBusiness {
    @Autowired
    private final SystemeCategorieEmprunteurRepository repository;

    public SystemeCategorieEmprunteurBusiness(SystemeCategorieEmprunteurRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeCategorieEmprunteur> findById(String codeCategorieEmprunteur) {
        return this.repository.findById(codeCategorieEmprunteur);
    }

    public List<SystemeCategorieEmprunteur> findAll() {
        return this.repository.findAll();
    }

    public SystemeCategorieEmprunteur save(SystemeCategorieEmprunteur categorieEmprunteur) {
        return this.repository.save(categorieEmprunteur);
    }
            
    public void delete(SystemeCategorieEmprunteur categorieEmprunteur) {
        this.repository.delete(categorieEmprunteur);
    } 
}
