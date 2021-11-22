/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.ContratAccompagnementDetails;
import com.progenia.incubatis01_03.data.entity.ContratAccompagnementDetailsId;
import com.progenia.incubatis01_03.data.repository.ContratAccompagnementDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ContratAccompagnementDetailsBusiness {
    @Autowired
    private final ContratAccompagnementDetailsRepository repository;

    public ContratAccompagnementDetailsBusiness(ContratAccompagnementDetailsRepository repository) {
        this.repository = repository;
    }

    public List<ContratAccompagnementDetails> getDetailsRelatedDataByNoContrat(Long noContrat) {
        return this.repository.findByContratAccompagnementDetailsIdNoContrat(noContrat);
    }
            
    public List<ContratAccompagnementDetails> getDetailsRelatedDataByCodeService(String codeService) {
        return this.repository.findByContratAccompagnementDetailsIdCodeService(codeService);
    }
            
    public List<ContratAccompagnementDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<ContratAccompagnementDetails> getContratAccompagnementDetails(Long noContrat, String codeService) {
        
        ContratAccompagnementDetailsId contratAccompagnementDetailsId = new ContratAccompagnementDetailsId();
        contratAccompagnementDetailsId.setNoContrat(noContrat);
        contratAccompagnementDetailsId.setCodeService(codeService);
            
        return this.repository.findById(contratAccompagnementDetailsId);
    }
    
    public ContratAccompagnementDetails save(ContratAccompagnementDetails contratAccompagnementDetails) {
        return this.repository.save(contratAccompagnementDetails);
    }
            
    public void delete(ContratAccompagnementDetails contratAccompagnementDetails) {
        this.repository.delete(contratAccompagnementDetails);
    }
}
