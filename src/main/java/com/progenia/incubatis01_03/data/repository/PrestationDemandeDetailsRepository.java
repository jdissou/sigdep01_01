/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.PrestationDemandeDetails;
import com.progenia.incubatis01_03.data.entity.PrestationDemandeDetailsId;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.VariableService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface PrestationDemandeDetailsRepository extends JpaRepository<PrestationDemandeDetails, PrestationDemandeDetailsId> {
        public List<PrestationDemandeDetails> findByPrestationDemandeDetailsIdNoPrestation(Long noPrestation);
        public List<PrestationDemandeDetails> findByPrestationDemandeDetailsIdCodeVariable(String codeVariable);
        
	public List<PrestationDemandeDetails> findByPrestationDemande_IsSaisieValideeAndPrestationDemande_CentreIncubateur_CodeCentreIncubateurAndPrestationDemande_Porteur_NoPorteurAndPrestationDemande_ServiceFourni_CodeServiceAndVariableService_CodeVariableAndPrestationDemande_DatePrestationBetween(Boolean isSaisieValidee, String codeCentreIncubateur, String noPorteur, String codeService, String codeVariable, LocalDate startDate, LocalDate endDate);
	public List<PrestationDemandeDetails> findByPrestationDemande_IsSaisieValideeAndPrestationDemande_CentreIncubateurAndPrestationDemande_PorteurAndPrestationDemande_ServiceFourniAndVariableServiceAndPrestationDemande_DatePrestationBetween(Boolean isSaisieValidee, CentreIncubateur centreIncubateur, Porteur porteur, ServiceFourni serviceFourni, VariableService variableService, LocalDate startDate, LocalDate endDate);
}
