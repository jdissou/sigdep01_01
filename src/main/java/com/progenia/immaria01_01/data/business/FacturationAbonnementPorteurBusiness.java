/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.FacturationAbonnement;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementPorteur;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementPorteurId;
import com.progenia.immaria01_01.data.entity.PeriodeFacturation;
import com.progenia.immaria01_01.data.entity.SequenceFacturation;
import com.progenia.immaria01_01.data.repository.FacturationAbonnementPorteurRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class FacturationAbonnementPorteurBusiness {
    @Autowired
    private final FacturationAbonnementPorteurRepository repository;

    public FacturationAbonnementPorteurBusiness(FacturationAbonnementPorteurRepository repository) {
        this.repository = repository;
    }

    public List<FacturationAbonnementPorteur> getDetailsRelatedDataByNoFacturation(Long noFacturation) {
        return this.repository.findByFacturationAbonnementPorteurIdNoFacturation(noFacturation);
    }
            
    public List<FacturationAbonnementPorteur> getDetailsRelatedDataByNoPorteur(String noPorteur) {
        return this.repository.findByFacturationAbonnementPorteurIdNoPorteur(noPorteur);
    }
            
    public List<FacturationAbonnementPorteur> findAll() {
        return this.repository.findAll();
    }

    public List<FacturationAbonnementPorteur> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(CentreIncubateur centreIncubateur, PeriodeFacturation periodeFacturation, SequenceFacturation sequenceFacturation) {
        return this.repository.findByFacturationAbonnementCentreIncubateurAndFacturationAbonnementPeriodeFacturationAndFacturationAbonnementSequenceFacturation(centreIncubateur, periodeFacturation, sequenceFacturation);
    }

    public Optional<FacturationAbonnementPorteur> findById(FacturationAbonnementPorteurId facturationAbonnementPorteurId) {
        return this.repository.findById(facturationAbonnementPorteurId);
    }

    public Optional<FacturationAbonnementPorteur> getFacturationAbonnementPorteur(Long noFacturation, String noPorteur) {
        
        FacturationAbonnementPorteurId facturationAbonnementPorteurId = new FacturationAbonnementPorteurId();
        facturationAbonnementPorteurId.setNoFacturation(noFacturation);
        facturationAbonnementPorteurId.setNoPorteur(noPorteur);
            
        return this.repository.findById(facturationAbonnementPorteurId);
    }
    
    public FacturationAbonnementPorteur save(FacturationAbonnementPorteur facturationAbonnementPorteur) {
        return this.repository.save(facturationAbonnementPorteur);
    }
            
    public void delete(FacturationAbonnementPorteur facturationAbonnementPorteur) {
        this.repository.delete(facturationAbonnementPorteur);
    }
}
