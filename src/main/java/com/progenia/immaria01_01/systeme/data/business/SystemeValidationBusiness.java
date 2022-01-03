/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeValidation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeValidationRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeValidationBusiness {
    @Autowired
    private final SystemeValidationRepository repository;

    public SystemeValidationBusiness(SystemeValidationRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeValidation> findById(String codeValidation) {
        return this.repository.findById(codeValidation);
    }

    public List<SystemeValidation> findAll() {
        return this.repository.findAll();
    }

    public SystemeValidation save(SystemeValidation validation) {
        return this.repository.save(validation);
    }
            
    public void delete(SystemeValidation validation) {
        this.repository.delete(validation);
    }
    
}
