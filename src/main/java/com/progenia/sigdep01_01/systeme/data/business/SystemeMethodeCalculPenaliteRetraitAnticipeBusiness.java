/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.business;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeMethodeCalculPenaliteRetraitAnticipe;
import com.progenia.sigdep01_01.systeme.data.repository.SystemeMethodeCalculPenaliteRetraitAnticipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeMethodeCalculPenaliteRetraitAnticipeBusiness {
    @Autowired
    private final SystemeMethodeCalculPenaliteRetraitAnticipeRepository repository;

    public SystemeMethodeCalculPenaliteRetraitAnticipeBusiness(SystemeMethodeCalculPenaliteRetraitAnticipeRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeMethodeCalculPenaliteRetraitAnticipe> findById(String codeMethodeCalcul) {
        return this.repository.findById(codeMethodeCalcul);
    }

    public List<SystemeMethodeCalculPenaliteRetraitAnticipe> findAll() {
        return this.repository.findAll();
    }

    public SystemeMethodeCalculPenaliteRetraitAnticipe save(SystemeMethodeCalculPenaliteRetraitAnticipe methodeCalcul) {
        return this.repository.save(methodeCalcul);
    }
            
    public void delete(SystemeMethodeCalculPenaliteRetraitAnticipe methodeCalcul) {
        this.repository.delete(methodeCalcul);
    } 
}
