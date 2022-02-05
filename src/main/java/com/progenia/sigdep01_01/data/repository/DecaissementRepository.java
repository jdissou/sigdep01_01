/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.Decaissement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface DecaissementRepository extends JpaRepository<Decaissement, Long> {
    public List<Decaissement> findByIsSaisieValidee(Boolean isSaisieValidee);
	public List<Decaissement> findByIsSaisieValideeAndDateDecaissementBetween(Boolean isSaisieValidee, LocalDate startDate, LocalDate endDate);
}


