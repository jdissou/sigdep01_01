/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.Mentor;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.MentorRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MentorBusiness {
    @Autowired
    private final MentorRepository repository;

    public MentorBusiness(MentorRepository repository) {
        this.repository = repository;
    }

    public Optional<Mentor> findById(String codeMentor) {
        return this.repository.findById(codeMentor);
    }

    public List<Mentor> findAll() {
        return this.repository.findAll();
    }

    public Mentor save(Mentor mentor) {
        return this.repository.save(mentor);
    }
            
    public void delete(Mentor mentor) {
        this.repository.delete(mentor);
    }
    
    public List<Mentor> getReportData() {
        return this.repository.findAll();
    }
}
