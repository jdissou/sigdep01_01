/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.business;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeNatureService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.systeme.data.repository.SystemeNatureServiceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeNatureServiceBusiness {
    @Autowired
    private final SystemeNatureServiceRepository repository;

    public SystemeNatureServiceBusiness(SystemeNatureServiceRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeNatureService> findById(String codeNatureService) {
        return this.repository.findById(codeNatureService);
    }

    public List<SystemeNatureService> findAll() {
        return this.repository.findAll();
    }

    public SystemeNatureService save(SystemeNatureService natureService) {
        return this.repository.save(natureService);
    }
            
    public void delete(SystemeNatureService natureService) {
        this.repository.delete(natureService);
    } 
}
