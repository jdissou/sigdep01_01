/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnementConsommation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementConsommationRepository extends JpaRepository<ZZZFacturationAbonnementConsommation, ZZZFacturationAbonnementConsommationId> {
        public List<ZZZFacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdNoFacturation(Long noFacturation);
        public List<ZZZFacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdNoInstrument(String noInstrument);
        public List<ZZZFacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdCodeService(String codeService);
        public List<ZZZFacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdCodeVariable(String codeVariable);

        public List<ZZZFacturationAbonnementConsommation> findByFacturationAbonnement_IsConsommationValideeAndFacturationAbonnement_CentreIncubateurAndFacturationAbonnement_NoFacturationAndInstrumentAndServiceFourniAndVariableService(Boolean isConsommationValidee, ZZZCentreIncubateur centreIncubateur, Long noFacturation, Instrument Instrument, ServiceFourni serviceFourni, VariableService variableService);
}

