/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.PrestationDemande;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface PrestationDemandeRepository extends JpaRepository<PrestationDemande, Long> {
        public List<PrestationDemande> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(Boolean isSaisieValidee, String codeCentreIncubateur);
	public List<PrestationDemande> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDatePrestationBetween(Boolean isSaisieValidee, String codeCentreIncubateur, LocalDate startDate, LocalDate endDate);
        public List<PrestationDemande> findByIsSaisieValideeAndIsPriseEnChargeAndCentreIncubateurAndPorteurAndDatePrestationBetween(Boolean isSaisieValidee, Boolean isPriseEnCharge, CentreIncubateur centreIncubateur, Porteur porteur, LocalDate startDate, LocalDate endDate);
	public List<PrestationDemande> findByIsSaisieValideeAndIsPriseEnChargeAndCentreIncubateurCodeCentreIncubateurAndPorteurNoPorteurAndDatePrestationBetween(Boolean isSaisieValidee, Boolean isPriseEnCharge, String codeCentreIncubateur, String noPorteur, LocalDate startDate, LocalDate endDate);
}


