/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.data.business;

import com.progenia.incubatis01_03.securities.data.entity.SystemeAutorisation;
import com.progenia.incubatis01_03.securities.data.entity.SystemeAutorisationId;
import com.progenia.incubatis01_03.securities.data.pojo.AutorisationUtilisateurPojo;
import com.progenia.incubatis01_03.securities.data.repository.SystemeAutorisationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeAutorisationBusiness {
    @Autowired
    private final SystemeAutorisationRepository repository;

    public SystemeAutorisationBusiness(SystemeAutorisationRepository repository) {
        this.repository = repository;
    }

    public List<SystemeAutorisation> findByAutorisationIdCodeCategorieUtilisateur(String codeCategorieUtilisateur) {
        return this.repository.findByAutorisationIdCodeCategorieUtilisateur(codeCategorieUtilisateur);
    }
            
    public List<SystemeAutorisation> findByAutorisationIdCodeCommande(String codeCommande) {
        return this.repository.findByAutorisationIdCodeCommande(codeCommande);
    }
            
    public List<SystemeAutorisation> findAll() {
        return this.repository.findAll();
    }

    public List<AutorisationUtilisateurPojo> getAutorisationUtilisateur(String codeUtilisateur, String codeCommande) {
        return this.repository.getAutorisationUtilisateur(codeUtilisateur, codeCommande);
    }
    

    public Optional<SystemeAutorisation> getSystemeAutorisation(String codeCategorieUtilisateur, String codeCommande) {
        
        SystemeAutorisationId systemeAutorisationId = new SystemeAutorisationId();
        systemeAutorisationId.setCodeCategorieUtilisateur(codeCategorieUtilisateur);
        systemeAutorisationId.setCodeCommande(codeCommande);
            
        return this.repository.findById(systemeAutorisationId);
    }
    
    public SystemeAutorisation save(SystemeAutorisation systemeAutorisation) {
        return this.repository.save(systemeAutorisation);
    }
            
    public void delete(SystemeAutorisation systemeAutorisation) {
        this.repository.delete(systemeAutorisation);
    }
}
