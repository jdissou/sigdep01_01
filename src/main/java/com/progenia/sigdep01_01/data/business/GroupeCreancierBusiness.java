/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.GroupeCreancier;

import java.util.List;
import java.util.Optional;

import com.progenia.sigdep01_01.data.repository.GroupeCreancierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class GroupeCreancierBusiness {
    @Autowired
    private final GroupeCreancierRepository repository;

    public GroupeCreancierBusiness(GroupeCreancierRepository repository) {
        this.repository = repository;
    }

    public Optional<GroupeCreancier> findById(String codeGroupeCreancier) {
        return this.repository.findById(codeGroupeCreancier);
    }

    public List<GroupeCreancier> findAll() {
        return this.repository.findAll();
    }

    public GroupeCreancier save(GroupeCreancier bureauRegional) {
        return this.repository.save(bureauRegional);
    }
            
    public void delete(GroupeCreancier bureauRegional) {
        this.repository.delete(bureauRegional);
    }
    
    public List<GroupeCreancier> getReportData() {
        return this.repository.findAll();
    } 
}
