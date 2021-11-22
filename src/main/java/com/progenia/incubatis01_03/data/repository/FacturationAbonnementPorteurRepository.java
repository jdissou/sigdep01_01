/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnement;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementPorteur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementPorteurId;
import com.progenia.incubatis01_03.data.entity.PeriodeFacturation;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementPorteurRepository extends JpaRepository<FacturationAbonnementPorteur, FacturationAbonnementPorteurId> {
        public List<FacturationAbonnementPorteur> findByFacturationAbonnementPorteurIdNoFacturation(Long noFacturation);
        public List<FacturationAbonnementPorteur> findByFacturationAbonnementPorteurIdNoPorteur(String noPorteur);

	public List<FacturationAbonnementPorteur> findByFacturationAbonnementCentreIncubateurAndFacturationAbonnementPeriodeFacturationAndFacturationAbonnementSequenceFacturation(CentreIncubateur centreIncubateur, PeriodeFacturation periodeFacturation, SequenceFacturation sequenceFacturation);
}
