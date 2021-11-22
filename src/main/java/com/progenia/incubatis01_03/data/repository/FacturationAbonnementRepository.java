/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnement;
import com.progenia.incubatis01_03.data.entity.PeriodeFacturation;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementRepository extends JpaRepository<FacturationAbonnement, Long> {
	public List<FacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturationAndDateFacturationBetween(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation, LocalDate startDate, LocalDate endDate);
	public List<FacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation);

	public List<FacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturationAndDateFacturationBetween(CentreIncubateur centreIncubateur, PeriodeFacturation periodeFacturation, SequenceFacturation sequenceFacturation, LocalDate startDate, LocalDate endDate);
	public List<FacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(CentreIncubateur centreIncubateur, PeriodeFacturation periodeFacturation, SequenceFacturation sequenceFacturation);
}


