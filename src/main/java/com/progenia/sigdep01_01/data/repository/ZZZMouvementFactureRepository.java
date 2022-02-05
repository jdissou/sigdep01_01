/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.MouvementFacture;
import com.progenia.sigdep01_01.data.entity.Instrument;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementFactureRepository extends JpaRepository<MouvementFacture, Long> {

	public List<MouvementFacture> findByCentreIncubateurAndInstrumentAndDateMouvementLessThanEqual(ZZZCentreIncubateur centreIncubateur, Instrument Instrument, LocalDate dateEcheanceFacture);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    /*
    @Query("SELECT DISTINCT mouvementFacture.noMouvement AS noMouvementFacture, mouvementFacture.noChrono AS noChronoFacture, "
            + "mouvementFacture.dateMouvement AS dateFacture, mouvementFacture.Instrument AS Instrument, mouvementFacture.typeFacture AS typeFacture, "
            + "(mouvementFacture.montantFacture - mouvementFacture.totalReglement) AS montantFactureAttendu "
            + "FROM MouvementFacture mouvementFacture WHERE mouvementFacture.centreIncubateur = :centreIncubateur AND mouvementFacture.Instrument = :Instrument AND mouvementFacture.dateMouvement <= :dateEcheanceFacture")
            //+ "FROM MouvementFacture mouvementFacture WHERE mouvementFacture.centreIncubateur = ?1 AND mouvementFacture.Instrument = ?2 AND mouvementFacture.dateMouvement <= ?3")
    public List<ZZZIReglementInstrumentDetailsSource> getReglementInstrumentDetailsSource(ZZZCentreIncubateur centreIncubateur, Instrument Instrument, LocalDate dateEcheanceFacture);
    */
}


/*

*/