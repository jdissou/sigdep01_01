/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.MouvementFinancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementFinancierRepository extends JpaRepository<MouvementFinancier, Long> {
    /*
    public List<MouvementFinancier> findByIsSaisieValidee(Boolean isSaisieValidee);
    public List<MouvementFinancier> findByIsSaisieValideeAndDateMouvementBetween(Boolean isSaisieValidee, LocalDate startDate, LocalDate endDate);
     */
    public List<MouvementFinancier> findByDateMouvementBetween(LocalDate startDate, LocalDate endDate);
}


