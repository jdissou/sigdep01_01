/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.ComptePojo;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.data.pojo.SoldeMouvementComptaPojo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.CompteRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CompteBusiness {
    @Autowired
    private final CompteRepository repository;

    public CompteBusiness(CompteRepository repository) {
        this.repository = repository;
    }

    public Optional<Compte> findById(String codeCompte) {
        return this.repository.findById(codeCompte);
    }

    public List<Compte> findAll() {
        return this.repository.findAll();
    }


    public List<Compte> findByRegroupementFalse(){
        return this.repository.findByRegroupementFalse();
    }

    public List<Compte> findByRegroupementFalseAndNoCompteNot(String noCompte){
        return this.repository.findByRegroupementFalseAndNoCompteNot(noCompte);
    }

    public Compte save(Compte compte) {
        return this.repository.save(compte);
    }
            
    public void delete(Compte compte) {
        this.repository.delete(compte);
    }
    public List<SoldeMouvementComptaPojo> getSoldeMouvementCompta(String codeCentreIncubateur, String noCompte, Integer noExercice) {
        return this.repository.getSoldeMouvementCompta(codeCentreIncubateur, noCompte, noExercice);
    }
            
     public List<ComptePojo> getReportData() {
        return this.repository.getReportData();
    }
}
