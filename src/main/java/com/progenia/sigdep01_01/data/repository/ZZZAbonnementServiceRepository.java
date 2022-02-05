/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZAbonnementService;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ZZZAbonnementServiceRepository extends JpaRepository<ZZZAbonnementService, Long> {
    public List<ZZZAbonnementService> findByCentreIncubateurAndInstrument(ZZZCentreIncubateur centreIncubateur, Instrument Instrument);
    
    public List<ZZZAbonnementService> findByIsClotureAndCentreIncubateurAndInstrumentAndServiceFourni(Boolean isCloture, ZZZCentreIncubateur centreIncubateur, Instrument Instrument, ServiceFourni serviceFourni);
    public List<ZZZAbonnementService> findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndInstrument_NoInstrumentAndServiceFourni_CodeService(Boolean isCloture, String codeCentreIncubateur, String noInstrument, String codeService);

    public List<ZZZAbonnementService> findByIsClotureAndCentreIncubateurAndInstrument(Boolean isCloture, ZZZCentreIncubateur centreIncubateur, Instrument Instrument);
    public List<ZZZAbonnementService> findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndInstrument_NoInstrument(Boolean isCloture, String codeCentreIncubateur, String noInstrument);

    public List<ZZZAbonnementService> findByIsClotureAndCentreIncubateurAndServiceFourni(Boolean isCloture, ZZZCentreIncubateur centreIncubateur, ServiceFourni serviceFourni);
    public List<ZZZAbonnementService> findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndServiceFourni_CodeService(Boolean isCloture, String codeCentreIncubateur, String codeService);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.Instrument AS Instrument FROM ZZZAbonnementService abonnementService, Instrument Instrument WHERE abonnementService.Instrument = Instrument AND Instrument.isArchive = false AND Instrument.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND Instrument.sequenceFacturation = :sequenceFacturation")
    public List<ZZZIFacturationAbonnementInstrumentSource> getFacturationAbonnementInstrumentSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.Instrument AS Instrument, abonnementService.serviceFourni AS serviceFourni, abonnementService.noChrono AS noChronoAbonnement FROM ZZZAbonnementService abonnementService, Instrument Instrument WHERE abonnementService.Instrument = Instrument AND Instrument.isArchive = false AND Instrument.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND Instrument.sequenceFacturation = :sequenceFacturation")
    public List<ZZZIFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.Instrument AS Instrument, abonnementService.serviceFourni AS serviceFourni, abonnementService.noChrono AS noChronoAbonnement FROM ZZZAbonnementService abonnementService, Instrument Instrument WHERE abonnementService.Instrument = Instrument AND Instrument.isArchive = false AND Instrument.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND Instrument.sequenceFacturation = :sequenceFacturation AND abonnementService.Instrument = :Instrument")
    public List<ZZZIFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation, Instrument Instrument);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.Instrument AS Instrument, abonnementService.serviceFourni AS serviceFourni, serviceFourniParametrage.variableService AS variableService FROM ZZZAbonnementService abonnementService, Instrument Instrument, XXXServiceFourniParametrage serviceFourniParametrage WHERE abonnementService.Instrument = Instrument AND abonnementService.serviceFourni = serviceFourniParametrage.serviceFourni AND Instrument.isArchive = false AND Instrument.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND Instrument.sequenceFacturation = :sequenceFacturation")
    public List<ZZZIFacturationAbonnementConsommationSource> getFacturationAbonnementConsommationSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation);
}

