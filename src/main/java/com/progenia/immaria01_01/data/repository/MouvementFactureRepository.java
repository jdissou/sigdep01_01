/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.IReglementPorteurDetailsSource;
import com.progenia.immaria01_01.data.entity.MouvementFacture;
import com.progenia.immaria01_01.data.entity.Porteur;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementFactureRepository extends JpaRepository<MouvementFacture, Long> {

	public List<MouvementFacture> findByCentreIncubateurAndPorteurAndDateMouvementLessThanEqual(CentreIncubateur centreIncubateur, Porteur porteur, LocalDate dateEcheanceFacture);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    /*
    @Query("SELECT DISTINCT mouvementFacture.noMouvement AS noMouvementFacture, mouvementFacture.noChrono AS noChronoFacture, "
            + "mouvementFacture.dateMouvement AS dateFacture, mouvementFacture.porteur AS porteur, mouvementFacture.typeFacture AS typeFacture, "
            + "(mouvementFacture.montantFacture - mouvementFacture.totalReglement) AS montantFactureAttendu "
            + "FROM MouvementFacture mouvementFacture WHERE mouvementFacture.centreIncubateur = :centreIncubateur AND mouvementFacture.porteur = :porteur AND mouvementFacture.dateMouvement <= :dateEcheanceFacture")
            //+ "FROM MouvementFacture mouvementFacture WHERE mouvementFacture.centreIncubateur = ?1 AND mouvementFacture.porteur = ?2 AND mouvementFacture.dateMouvement <= ?3")
    public List<IReglementPorteurDetailsSource> getReglementPorteurDetailsSource(CentreIncubateur centreIncubateur, Porteur porteur, LocalDate dateEcheanceFacture);
    */
}


/*

*/