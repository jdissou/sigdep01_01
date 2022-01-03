/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.ParametreSysteme;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ParametreSystemeRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ParametreSystemeBusiness {
    @Autowired
    private final ParametreSystemeRepository repository;

    public ParametreSystemeBusiness(ParametreSystemeRepository repository) {
        this.repository = repository;
    }

    public Optional<ParametreSysteme> findById(String codeParametre) {
        return this.repository.findById(codeParametre);
    }

    public List<ParametreSysteme> findAll() {
        return this.repository.findAll();
    }

    public ParametreSysteme save(ParametreSysteme parametreSysteme) {
        return this.repository.save(parametreSysteme);
    }
            
    public void delete(ParametreSysteme parametreSysteme) {
        this.repository.delete(parametreSysteme);
    }

    public Integer count() {
        return Math.toIntExact(this.repository.count());
    }

        
}
