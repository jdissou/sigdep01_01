/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Constante;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ConstanteRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ConstanteBusiness {
    @Autowired
    private final ConstanteRepository repository;

    public ConstanteBusiness(ConstanteRepository repository) {
        this.repository = repository;
    }

    public Optional<Constante> findById(String codeConstante) {
        return this.repository.findById(codeConstante);
    }

    public List<Constante> findAll() {
        return this.repository.findAll();
    }

    public Constante save(Constante constante) {
        return this.repository.save(constante);
    }
            
    public void delete(Constante constante) {
        this.repository.delete(constante);
    }

    public List<Constante> getReportData() {
        return this.repository.findAll();
    }    
}
