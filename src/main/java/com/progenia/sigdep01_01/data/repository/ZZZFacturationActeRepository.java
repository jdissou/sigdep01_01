/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.ZZZFacturationActe;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationActeRepository extends JpaRepository<ZZZFacturationActe, Long> {
        public List<ZZZFacturationActe> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(Boolean isSaisieValidee, String codeCentreIncubateur);
	public List<ZZZFacturationActe> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateFacturationBetween(Boolean isSaisieValidee, String codeCentreIncubateur, LocalDate startDate, LocalDate endDate);
}


