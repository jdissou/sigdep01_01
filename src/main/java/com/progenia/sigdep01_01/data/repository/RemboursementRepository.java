/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.Remboursement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface RemboursementRepository extends JpaRepository<Remboursement, Long> {
        public List<Remboursement> findByIsSaisieValidee(Boolean isSaisieValidee);
	public List<Remboursement> findByIsSaisieValideeAndDateRemboursementBetween(Boolean isSaisieValidee, LocalDate startDate, LocalDate endDate);
}


