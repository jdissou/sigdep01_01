/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Exercice;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ExerciceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ExerciceBusiness {
    @Autowired
    private final ExerciceRepository repository;

    public ExerciceBusiness(ExerciceRepository repository) {
        this.repository = repository;
    }

    public Optional<Exercice> findById(Short noExercice) {
        return this.repository.findById(noExercice);
    }

    public List<Exercice> findAll() {
        return this.repository.findAll();
    }
    
    public List<Exercice> findByNoExerciceNot(Integer noExercice) {
        return this.repository.findByNoExerciceNot(noExercice);
    }

    public Exercice save(Exercice exercice) {
        return this.repository.save(exercice);
    }
            
    public void delete(Exercice exercice) {
        this.repository.delete(exercice);
    }
       
    public List<Exercice> getReportData() {
        return this.repository.findAll();
    } 
 
}
