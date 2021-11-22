/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.SequenceFacturationRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SequenceFacturationBusiness {
    @Autowired
    private final SequenceFacturationRepository repository;

    public SequenceFacturationBusiness(SequenceFacturationRepository repository) {
        this.repository = repository;
    }

    public Optional<SequenceFacturation> findById(String codeSequenceFacturation) {
        return this.repository.findById(codeSequenceFacturation);
    }

    public List<SequenceFacturation> findAll() {
        return this.repository.findAll();
    }

    public SequenceFacturation save(SequenceFacturation sequenceVirementSalaire) {
        return this.repository.save(sequenceVirementSalaire);
    }
            
    public void delete(SequenceFacturation sequenceVirementSalaire) {
        this.repository.delete(sequenceVirementSalaire);
    }
    
    public List<SequenceFacturation> getReportData() {
        return this.repository.findAll();
    }

}
