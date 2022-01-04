/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Immeuble;
import com.progenia.immaria01_01.data.pojo.ImmeublePojo;
import com.progenia.immaria01_01.data.repository.ImmeubleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ImmeubleBusiness {
    @Autowired
    private final ImmeubleRepository repository;

    public ImmeubleBusiness(ImmeubleRepository repository) {
        this.repository = repository;
    }

    public Optional<Immeuble> findById(String codeImmeuble) {
        return this.repository.findById(codeImmeuble);
    }

    public List<Immeuble> findAll() {
        return this.repository.findAll();
    }

    public Immeuble save(Immeuble immeuble) {
        return this.repository.save(immeuble);
    }
            
    public void delete(Immeuble immeuble) {
        this.repository.delete(immeuble);
    }
        
    public List<ImmeublePojo> getReportData() {
        return this.repository.getReportData();
    }
}
