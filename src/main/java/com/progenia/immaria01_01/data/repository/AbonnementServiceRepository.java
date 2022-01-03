/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.AbonnementService;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.IFacturationAbonnementConsommationSource;
import com.progenia.immaria01_01.data.entity.IFacturationAbonnementDetailsSource;
import com.progenia.immaria01_01.data.entity.IFacturationAbonnementPorteurSource;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.SequenceFacturation;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface AbonnementServiceRepository extends JpaRepository<AbonnementService, Long> {
    public List<AbonnementService> findByCentreIncubateurAndPorteur(CentreIncubateur centreIncubateur, Porteur porteur);
    
    public List<AbonnementService> findByIsClotureAndCentreIncubateurAndPorteurAndServiceFourni(Boolean isCloture, CentreIncubateur centreIncubateur, Porteur porteur, ServiceFourni serviceFourni);
    public List<AbonnementService> findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndPorteur_NoPorteurAndServiceFourni_CodeService(Boolean isCloture, String codeCentreIncubateur, String noPorteur, String codeService);

    public List<AbonnementService> findByIsClotureAndCentreIncubateurAndPorteur(Boolean isCloture, CentreIncubateur centreIncubateur, Porteur porteur);
    public List<AbonnementService> findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndPorteur_NoPorteur(Boolean isCloture, String codeCentreIncubateur, String noPorteur);

    public List<AbonnementService> findByIsClotureAndCentreIncubateurAndServiceFourni(Boolean isCloture, CentreIncubateur centreIncubateur, ServiceFourni serviceFourni);
    public List<AbonnementService> findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndServiceFourni_CodeService(Boolean isCloture, String codeCentreIncubateur, String codeService);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.porteur AS porteur FROM AbonnementService abonnementService, Porteur porteur WHERE abonnementService.porteur = porteur AND porteur.isArchive = false AND porteur.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND porteur.sequenceFacturation = :sequenceFacturation")
    public List<IFacturationAbonnementPorteurSource> getFacturationAbonnementPorteurSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.porteur AS porteur, abonnementService.serviceFourni AS serviceFourni, abonnementService.noChrono AS noChronoAbonnement FROM AbonnementService abonnementService, Porteur porteur WHERE abonnementService.porteur = porteur AND porteur.isArchive = false AND porteur.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND porteur.sequenceFacturation = :sequenceFacturation")
    public List<IFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.porteur AS porteur, abonnementService.serviceFourni AS serviceFourni, abonnementService.noChrono AS noChronoAbonnement FROM AbonnementService abonnementService, Porteur porteur WHERE abonnementService.porteur = porteur AND porteur.isArchive = false AND porteur.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND porteur.sequenceFacturation = :sequenceFacturation AND abonnementService.porteur = :porteur")
    public List<IFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation, Porteur porteur);

    /* Customizing the Result of JPA Queries with Aggregation Functions with Spring Data Projection.
    Query method - Using @Query annotation - JPA query
    To use iynterface-based projection, we must define a Java interface composed of getter methods that match the projected attribute names.
    To allow Spring to bind the projected values to our interface, we need to give aliases to each projected attribute with the property name found in the interface.
    */
    @Query("SELECT DISTINCT abonnementService.porteur AS porteur, abonnementService.serviceFourni AS serviceFourni, serviceFourniParametrage.variableService AS variableService FROM AbonnementService abonnementService, Porteur porteur, ServiceFourniParametrage serviceFourniParametrage WHERE abonnementService.porteur = porteur AND abonnementService.serviceFourni = serviceFourniParametrage.serviceFourni AND porteur.isArchive = false AND porteur.isInactif = false AND abonnementService.isCloture = false AND abonnementService.centreIncubateur = :centreIncubateur AND porteur.sequenceFacturation = :sequenceFacturation")
    public List<IFacturationAbonnementConsommationSource> getFacturationAbonnementConsommationSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation);
}

