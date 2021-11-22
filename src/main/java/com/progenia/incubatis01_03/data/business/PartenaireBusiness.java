/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.PartenairePojo;
import com.progenia.incubatis01_03.data.entity.Partenaire;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.PartenaireRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PartenaireBusiness {
    @Autowired
    private final PartenaireRepository repository;

    public PartenaireBusiness(PartenaireRepository repository) {
        this.repository = repository;
    }

    public Optional<Partenaire> findById(String codePartenaire) {
        return this.repository.findById(codePartenaire);
    }

    public List<Partenaire> findAll() {
        return this.repository.findAll();
    }

    public Partenaire save(Partenaire partenaire) {
        return this.repository.save(partenaire);
    }
            
    public void delete(Partenaire partenaire) {
        this.repository.delete(partenaire);
    }
    
    public List<PartenairePojo> getReportData() {
        return this.repository.getReportData();
    }
}
