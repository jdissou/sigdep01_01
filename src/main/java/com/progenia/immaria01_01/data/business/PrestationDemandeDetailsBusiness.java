/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.PrestationDemandeDetails;
import com.progenia.immaria01_01.data.entity.PrestationDemandeDetailsId;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import com.progenia.immaria01_01.data.entity.VariableService;
import com.progenia.immaria01_01.data.repository.PrestationDemandeDetailsRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class PrestationDemandeDetailsBusiness {
    @Autowired
    private final PrestationDemandeDetailsRepository repository;

    public PrestationDemandeDetailsBusiness(PrestationDemandeDetailsRepository repository) {
        this.repository = repository;
    }

    public List<PrestationDemandeDetails> getDetailsRelatedDataByNoPrestation(Long noPrestation) {
        return this.repository.findByPrestationDemandeDetailsIdNoPrestation(noPrestation);
    }
            
    public List<PrestationDemandeDetails> getDetailsRelatedDataByCodeVariable(String codeVariable) {
        return this.repository.findByPrestationDemandeDetailsIdCodeVariable(codeVariable);
    }
   
    public List<PrestationDemandeDetails> getPrestationDemandeVariableConsommeeByCodeCentreIncubateurAndNoPorteurAndCodeServiceAndCodeVariableAndDatePrestationBetween(String codeCentreIncubateur, String noPorteur, String codeService, String codeVariable, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByPrestationDemande_IsSaisieValideeAndPrestationDemande_CentreIncubateur_CodeCentreIncubateurAndPrestationDemande_Porteur_NoPorteurAndPrestationDemande_ServiceFourni_CodeServiceAndVariableService_CodeVariableAndPrestationDemande_DatePrestationBetween(true, codeCentreIncubateur, noPorteur, codeService, codeVariable, startDate, endDate);
    }
   
    public List<PrestationDemandeDetails> getPrestationDemandeVariableConsommeeByCentreIncubateurAndPorteurAndServiceFourniAndVariableServiceAndDatePrestationBetween(CentreIncubateur centreIncubateur, Porteur porteur, ServiceFourni serviceFourni, VariableService variableService, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByPrestationDemande_IsSaisieValideeAndPrestationDemande_CentreIncubateurAndPrestationDemande_PorteurAndPrestationDemande_ServiceFourniAndVariableServiceAndPrestationDemande_DatePrestationBetween(true, centreIncubateur, porteur, serviceFourni, variableService, startDate, endDate);
    }
   
    public List<PrestationDemandeDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<PrestationDemandeDetails> findById(PrestationDemandeDetailsId prestationDemandeDetailsId) {
        return this.repository.findById(prestationDemandeDetailsId);
    }

    public Optional<PrestationDemandeDetails> getPrestationDemandeDetails(Long noPrestation, String codeVariable) {
        
        PrestationDemandeDetailsId prestationDemandeDetailsId = new PrestationDemandeDetailsId();
        prestationDemandeDetailsId.setNoPrestation(noPrestation);
        prestationDemandeDetailsId.setCodeVariable(codeVariable);
            
        return this.repository.findById(prestationDemandeDetailsId);
    }
    
    public PrestationDemandeDetails save(PrestationDemandeDetails prestationDemandeDetails) {
        return this.repository.save(prestationDemandeDetails);
    }
            
    public void delete(PrestationDemandeDetails prestationDemandeDetails) {
        this.repository.delete(prestationDemandeDetails);
    }
}
