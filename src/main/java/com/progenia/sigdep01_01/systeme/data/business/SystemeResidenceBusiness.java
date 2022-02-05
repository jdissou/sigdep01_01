/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeResidence;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeResidenceBusiness {
    @Autowired
    private final SystemeResidenceRepository repository;

    public SystemeResidenceBusiness(SystemeResidenceRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeResidence> findById(String codeResidence) {
        return this.repository.findById(codeResidence);
    }

    public List<SystemeResidence> findAll() {
        return this.repository.findAll();
    }

    public SystemeResidence save(SystemeResidence residence) {
        return this.repository.save(residence);
    }
            
    public void delete(SystemeResidence residence) {
        this.repository.delete(residence);
    } 
}
