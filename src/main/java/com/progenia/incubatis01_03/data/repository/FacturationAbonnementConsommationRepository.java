/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementConsommation;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementConsommationId;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.VariableService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementConsommationRepository extends JpaRepository<FacturationAbonnementConsommation, FacturationAbonnementConsommationId> {
        public List<FacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdNoFacturation(Long noFacturation);
        public List<FacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdNoPorteur(String noPorteur);
        public List<FacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdCodeService(String codeService);
        public List<FacturationAbonnementConsommation> findByFacturationAbonnementConsommationIdCodeVariable(String codeVariable);

        public List<FacturationAbonnementConsommation> findByFacturationAbonnement_IsConsommationValideeAndFacturationAbonnement_CentreIncubateurAndFacturationAbonnement_NoFacturationAndPorteurAndServiceFourniAndVariableService(Boolean isConsommationValidee, CentreIncubateur centreIncubateur, Long noFacturation, Porteur porteur, ServiceFourni serviceFourni, VariableService variableService);
}

