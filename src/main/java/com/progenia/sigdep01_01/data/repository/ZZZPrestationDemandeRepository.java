/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.Instrument;
import com.progenia.sigdep01_01.data.entity.PrestationDemande;
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
        public List<PrestationDemande> findByIsSaisieValideeAndIsPriseEnChargeAndCentreIncubateurAndInstrumentAndDatePrestationBetween(Boolean isSaisieValidee, Boolean isPriseEnCharge, ZZZCentreIncubateur centreIncubateur, Instrument Instrument, LocalDate startDate, LocalDate endDate);
	public List<PrestationDemande> findByIsSaisieValideeAndIsPriseEnChargeAndCentreIncubateurCodeCentreIncubateurAndInstrumentNoInstrumentAndDatePrestationBetween(Boolean isSaisieValidee, Boolean isPriseEnCharge, String codeCentreIncubateur, String noInstrument, LocalDate startDate, LocalDate endDate);
}


