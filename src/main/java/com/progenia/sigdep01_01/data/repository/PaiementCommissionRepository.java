/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.PaiementCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface PaiementCommissionRepository extends JpaRepository<PaiementCommission, Long> {
        public List<PaiementCommission> findByIsSaisieValidee(Boolean isSaisieValidee);
	public List<PaiementCommission> findByIsSaisieValideeAndDatePaiementBetween(Boolean isSaisieValidee, LocalDate startDate, LocalDate endDate);
}


