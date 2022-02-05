/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.Remboursement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.RemboursementRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class RemboursementBusiness {
    @Autowired
    private final RemboursementRepository repository;

    public RemboursementBusiness(RemboursementRepository repository) {
        this.repository = repository;
    }

    public Optional<Remboursement> findById(Long noRemboursement) {
        return this.repository.findById(noRemboursement);
    }

    public List<Remboursement> findAll() {
        return this.repository.findAll();
    }

    public Remboursement save(Remboursement remboursement) {
        return this.repository.save(remboursement);
    }
            
    public void delete(Remboursement remboursement) {
        this.repository.delete(remboursement);
    }
    
    public List<Remboursement> getConsultationRemboursementListe(LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndDateRemboursementBetween(true, startDate, endDate);
    }
    
    public List<Remboursement> getBrouillardData() {
        return this.repository.findByIsSaisieValidee(false);
    } 
    
    public List<Remboursement> getValidatedData() {
        return this.repository.findByIsSaisieValidee(true);
    } 
}
