/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ProfilFinancementStructureRemboursement;
import com.progenia.sigdep01_01.data.entity.ProfilFinancementStructureRemboursementId;
import com.progenia.sigdep01_01.data.repository.ProfilFinancementStructureRemboursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ProfilFinancementStructureRemboursementBusiness {
    @Autowired
    private final ProfilFinancementStructureRemboursementRepository repository;

    public ProfilFinancementStructureRemboursementBusiness(ProfilFinancementStructureRemboursementRepository repository) {
        this.repository = repository;
    }

    public List<ProfilFinancementStructureRemboursement> getDetailsRelatedDataByCodeProfilFinancement(String codeProfilFinancement) {
        return this.repository.findByProfilFinancementStructureRemboursementIdCodeProfilFinancement(codeProfilFinancement);
    }

    public List<ProfilFinancementStructureRemboursement> getDetailsRelatedDataByCodeStructureRemboursement(String codeStructureRemboursement) {
        return this.repository.findByProfilFinancementStructureRemboursementIdCodeStructureRemboursement(codeStructureRemboursement);
    }

    public List<ProfilFinancementStructureRemboursement> findAll() {
        return this.repository.findAll();
    }

    public Optional<ProfilFinancementStructureRemboursement> findById(ProfilFinancementStructureRemboursementId profilFinancementStructureRemboursementId) {
        return this.repository.findById(profilFinancementStructureRemboursementId);
    }

    public ProfilFinancementStructureRemboursement save(ProfilFinancementStructureRemboursement profilFinancementStructureRemboursement) {
        return this.repository.save(profilFinancementStructureRemboursement);
    }
            
    public void delete(ProfilFinancementStructureRemboursement profilFinancementStructureRemboursement) {
        this.repository.delete(profilFinancementStructureRemboursement);
    }
}
