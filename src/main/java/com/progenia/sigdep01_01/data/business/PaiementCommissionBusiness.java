/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.PaiementCommission;
import com.progenia.sigdep01_01.data.repository.PaiementCommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PaiementCommissionBusiness {
    @Autowired
    private final PaiementCommissionRepository repository;

    public PaiementCommissionBusiness(PaiementCommissionRepository repository) {
        this.repository = repository;
    }

    public Optional<PaiementCommission> findById(Long noPaiement) {
        return this.repository.findById(noPaiement);
    }

    public List<PaiementCommission> findAll() {
        return this.repository.findAll();
    }

    public PaiementCommission save(PaiementCommission paiementCommission) {
        return this.repository.save(paiementCommission);
    }
            
    public void delete(PaiementCommission paiementCommission) {
        this.repository.delete(paiementCommission);
    }
    
    public List<PaiementCommission> getConsultationPaiementCommissionListe(LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndDatePaiementBetween(true, startDate, endDate);
    }
    
    public List<PaiementCommission> getBrouillardData() {
        return this.repository.findByIsSaisieValidee(false);
    } 
    
    public List<PaiementCommission> getValidatedData() {
        return this.repository.findByIsSaisieValidee(true);
    } 
}
