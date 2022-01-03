/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.UniteOeuvre;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.UniteOeuvreRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class UniteOeuvreBusiness {
    @Autowired
    private final UniteOeuvreRepository repository;

    public UniteOeuvreBusiness(UniteOeuvreRepository repository) {
        this.repository = repository;
    }

    public Optional<UniteOeuvre> findById(String codeUniteOeuvre) {
        return this.repository.findById(codeUniteOeuvre);
    }

    public List<UniteOeuvre> findAll() {
        return this.repository.findAll();
    }

    public UniteOeuvre save(UniteOeuvre UniteOeuvre) {
        return this.repository.save(UniteOeuvre);
    }
            
    public void delete(UniteOeuvre UniteOeuvre) {
        this.repository.delete(UniteOeuvre);
    }
    
    public List<UniteOeuvre> getReportData() {
        return this.repository.findAll();
    } 
    
}
