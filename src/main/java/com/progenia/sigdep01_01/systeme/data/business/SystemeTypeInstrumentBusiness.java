/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeInstrument;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeTypeInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeTypeInstrumentBusiness {
    @Autowired
    private final SystemeTypeInstrumentRepository repository;

    public SystemeTypeInstrumentBusiness(SystemeTypeInstrumentRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeTypeInstrument> findById(String codeTypeInstrument) {
        return this.repository.findById(codeTypeInstrument);
    }

    public List<SystemeTypeInstrument> findAll() {
        return this.repository.findAll();
    }

    public SystemeTypeInstrument save(SystemeTypeInstrument typeInstrument) {
        return this.repository.save(typeInstrument);
    }
            
    public void delete(SystemeTypeInstrument typeInstrument) {
        this.repository.delete(typeInstrument);
    }
    
}
