/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeTypeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeTypeServiceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeServiceBusiness {
    @Autowired
    private final SystemeTypeServiceRepository repository;

    public SystemeTypeServiceBusiness(SystemeTypeServiceRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeService> findById(String codeTypeService) {
        return this.repository.findById(codeTypeService);
    }

    public List<SystemeTypeService> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeService save(SystemeTypeService typeService) {
        return this.repository.save(typeService);
    }
            
    public void delete(SystemeTypeService typeService) {
        this.repository.delete(typeService);
    }
    
}
