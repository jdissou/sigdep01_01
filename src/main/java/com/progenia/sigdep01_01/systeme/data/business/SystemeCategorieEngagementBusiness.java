/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeCategorieEngagement;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeCategorieEngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeCategorieEngagementBusiness {
    @Autowired
    private final SystemeCategorieEngagementRepository repository;

    public SystemeCategorieEngagementBusiness(SystemeCategorieEngagementRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeCategorieEngagement> findById(String codeCategorieEngagement) {
        return this.repository.findById(codeCategorieEngagement);
    }

    public List<SystemeCategorieEngagement> findAll() {
        return this.repository.findAll();
    }

    public SystemeCategorieEngagement save(SystemeCategorieEngagement categorieEngagement) {
        return this.repository.save(categorieEngagement);
    }
            
    public void delete(SystemeCategorieEngagement categorieEngagement) {
        this.repository.delete(categorieEngagement);
    } 
}
