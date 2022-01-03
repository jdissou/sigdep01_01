/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.ContratAccompagnement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ContratAccompagnementRepository extends JpaRepository<ContratAccompagnement, Long> {
        public List<ContratAccompagnement> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(Boolean isSaisieValidee, String codeCentreIncubateur);
	public List<ContratAccompagnement> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateContratBetween(Boolean isSaisieValidee, String codeCentreIncubateur, LocalDate startDate, LocalDate endDate);
}


