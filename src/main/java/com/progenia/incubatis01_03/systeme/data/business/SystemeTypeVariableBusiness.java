/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.business;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeTypeVariable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.systeme.data.repository.SystemeTypeVariableRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeVariableBusiness {
    @Autowired
    private final SystemeTypeVariableRepository repository;

    public SystemeTypeVariableBusiness(SystemeTypeVariableRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeVariable> findById(String codeTypeVariable) {
        return this.repository.findById(codeTypeVariable);
    }

    public List<SystemeTypeVariable> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeVariable save(SystemeTypeVariable typeVariable) {
        return this.repository.save(typeVariable);
    }
            
    public void delete(SystemeTypeVariable typeVariable) {
        this.repository.delete(typeVariable);
    }
    
}
