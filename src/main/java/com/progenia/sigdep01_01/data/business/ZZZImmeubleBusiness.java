/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZImmeuble;
import com.progenia.sigdep01_01.data.pojo.ImmeublePojo;
import com.progenia.sigdep01_01.data.repository.ImmeubleRepository;
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

    public Optional<ZZZImmeuble> findById(String codeImmeuble) {
        return this.repository.findById(codeImmeuble);
    }

    public List<ZZZImmeuble> findAll() {
        return this.repository.findAll();
    }

    public ZZZImmeuble save(ZZZImmeuble immeuble) {
        return this.repository.save(immeuble);
    }
            
    public void delete(ZZZImmeuble immeuble) {
        this.repository.delete(immeuble);
    }
        
    public List<ImmeublePojo> getReportData() {
        return this.repository.getReportData();
    }
}
