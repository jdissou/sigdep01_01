/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.repository.PrestationDemandeDetailsRepository;
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
   
    public List<PrestationDemandeDetails> getPrestationDemandeVariableConsommeeByCodeCentreIncubateurAndNoInstrumentAndCodeServiceAndCodeVariableAndDatePrestationBetween(String codeCentreIncubateur, String noInstrument, String codeService, String codeVariable, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByPrestationDemande_IsSaisieValideeAndPrestationDemande_CentreIncubateur_CodeCentreIncubateurAndPrestationDemande_Instrument_NoInstrumentAndPrestationDemande_ServiceFourni_CodeServiceAndVariableService_CodeVariableAndPrestationDemande_DatePrestationBetween(true, codeCentreIncubateur, noInstrument, codeService, codeVariable, startDate, endDate);
    }
   
    public List<PrestationDemandeDetails> getPrestationDemandeVariableConsommeeByCentreIncubateurAndInstrumentAndServiceFourniAndVariableServiceAndDatePrestationBetween(ZZZCentreIncubateur centreIncubateur, Instrument Instrument, ServiceFourni serviceFourni, VariableService variableService, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByPrestationDemande_IsSaisieValideeAndPrestationDemande_CentreIncubateurAndPrestationDemande_InstrumentAndPrestationDemande_ServiceFourniAndVariableServiceAndPrestationDemande_DatePrestationBetween(true, centreIncubateur, Instrument, serviceFourni, variableService, startDate, endDate);
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
