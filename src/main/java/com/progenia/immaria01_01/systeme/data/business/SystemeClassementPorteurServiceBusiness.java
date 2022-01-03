/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeClassementPorteurService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeClassementPorteurServiceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeClassementPorteurServiceBusiness {
    @Autowired
    private final SystemeClassementPorteurServiceRepository repository;

    public SystemeClassementPorteurServiceBusiness(SystemeClassementPorteurServiceRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeClassementPorteurService> findById(String codeClassementPorteurService) {
        return this.repository.findById(codeClassementPorteurService);
    }

    public List<SystemeClassementPorteurService> findAll() {
        return this.repository.findAll();
    }

    public SystemeClassementPorteurService save(SystemeClassementPorteurService classementPorteurService) {
        return this.repository.save(classementPorteurService);
    }
            
    public void delete(SystemeClassementPorteurService classementPorteurService) {
        this.repository.delete(classementPorteurService);
    } 
}
