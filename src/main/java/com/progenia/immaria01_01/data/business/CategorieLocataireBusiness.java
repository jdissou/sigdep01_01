/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CategorieLocataire;
import com.progenia.immaria01_01.data.pojo.CategorieLocatairePojo;
import com.progenia.immaria01_01.data.repository.CategorieLocataireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CategorieLocataireBusiness {
    @Autowired
    private final CategorieLocataireRepository repository;

    public CategorieLocataireBusiness(CategorieLocataireRepository repository) {
        this.repository = repository;
    }

    public Optional<CategorieLocataire> findById(String codeCategorieLocataire) {
        return this.repository.findById(codeCategorieLocataire);
    }

    public List<CategorieLocataire> findAll() {
        return this.repository.findAll();
    }

    public List<CategorieLocataire> findByCompteClientIsNotNull() {
        return this.repository.findByCompteClientIsNotNull();
    }

    public CategorieLocataire save(CategorieLocataire categorieLocataire) {
        return this.repository.save(categorieLocataire);
    }
            
    public void delete(CategorieLocataire categorieLocataire) {
        this.repository.delete(categorieLocataire);
    }
        
        public List<CategorieLocatairePojo> getReportData() {
        return this.repository.getReportData();
    }
}
