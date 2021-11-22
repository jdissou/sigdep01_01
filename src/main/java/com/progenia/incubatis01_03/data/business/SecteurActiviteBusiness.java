/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.SecteurActivite;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.SecteurActiviteRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SecteurActiviteBusiness {
    @Autowired
    private final SecteurActiviteRepository repository;

    public SecteurActiviteBusiness(SecteurActiviteRepository repository) {
        this.repository = repository;
    }

    public Optional<SecteurActivite> findById(String codeSecteurActivite) {
        return this.repository.findById(codeSecteurActivite);
    }

    public List<SecteurActivite> findAll() {
        return this.repository.findAll();
    }

    public SecteurActivite save(SecteurActivite secteurActivite) {
        return this.repository.save(secteurActivite);
    }
            
    public void delete(SecteurActivite secteurActivite) {
        this.repository.delete(secteurActivite);
    }
    
    public List<SecteurActivite> getReportData() {
        return this.repository.findAll();
    } 
}
