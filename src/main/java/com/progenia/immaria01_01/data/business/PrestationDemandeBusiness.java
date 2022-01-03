/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.PrestationDemande;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.PrestationDemandeRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PrestationDemandeBusiness {
    @Autowired
    private final PrestationDemandeRepository repository;

    public PrestationDemandeBusiness(PrestationDemandeRepository repository) {
        this.repository = repository;
    }

    public Optional<PrestationDemande> findById(Long noPrestation) {
        return this.repository.findById(noPrestation);
    }

    public List<PrestationDemande> findAll() {
        return this.repository.findAll();
    }

    public PrestationDemande save(PrestationDemande prestationDemande) {
        return this.repository.save(prestationDemande);
    }
            
    public void delete(PrestationDemande prestationDemande) {
        this.repository.delete(prestationDemande);
    }
    
    public List<PrestationDemande> getConsultationPrestationDemandeListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDatePrestationBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<PrestationDemande> getFacturationActeDetailsSourceListe(CentreIncubateur centreIncubateur, Porteur porteur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndIsPriseEnChargeAndCentreIncubateurAndPorteurAndDatePrestationBetween(true, false, centreIncubateur, porteur, startDate, endDate);
    }

    public List<PrestationDemande> getFacturationActeDetailsSourceListe(String codeCentreIncubateur, String noPorteur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndIsPriseEnChargeAndCentreIncubateurCodeCentreIncubateurAndPorteurNoPorteurAndDatePrestationBetween(true, false, codeCentreIncubateur, noPorteur, startDate, endDate);
    }

    public List<PrestationDemande> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<PrestationDemande> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 
}
