/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.IndiceReference;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnement;
import com.progenia.sigdep01_01.data.entity.SequenceFacturation;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementRepository extends JpaRepository<ZZZFacturationAbonnement, Long> {
	public List<ZZZFacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturationAndDateFacturationBetween(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation, LocalDate startDate, LocalDate endDate);
	public List<ZZZFacturationAbonnement> findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(String codeCentreIncubateur, Integer noPeriode, String codeSequenceFacturation);

	public List<ZZZFacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturationAndDateFacturationBetween(ZZZCentreIncubateur centreIncubateur, IndiceReference periodeFacturation, SequenceFacturation sequenceFacturation, LocalDate startDate, LocalDate endDate);
	public List<ZZZFacturationAbonnement> findByCentreIncubateurAndPeriodeFacturationAndSequenceFacturation(ZZZCentreIncubateur centreIncubateur, IndiceReference periodeFacturation, SequenceFacturation sequenceFacturation);
}


