/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Locataire;
import com.progenia.immaria01_01.data.pojo.LocatairePojo;
import com.progenia.immaria01_01.data.repository.LocataireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class LocataireBusiness {
    @Autowired
    private final LocataireRepository repository;

    public LocataireBusiness(LocataireRepository repository) {
        this.repository = repository;
    }

    public Optional<Locataire> findById(String codeLocataire) {
        return this.repository.findById(codeLocataire);
    }

    public List<Locataire> findAll() {
        return this.repository.findAll();
    }

    public Locataire save(Locataire locataire) {
        return this.repository.save(locataire);
    }
            
    public void delete(Locataire locataire) {
        this.repository.delete(locataire);
    }
        
    public List<LocatairePojo> getReportData() {
        return this.repository.getReportData();
    }
}
