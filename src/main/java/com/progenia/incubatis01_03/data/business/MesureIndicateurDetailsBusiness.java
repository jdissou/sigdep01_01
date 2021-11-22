/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.MesureIndicateurDetails;
import com.progenia.incubatis01_03.data.entity.MesureIndicateurDetailsId;
import com.progenia.incubatis01_03.data.repository.MesureIndicateurDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MesureIndicateurDetailsBusiness {
    @Autowired
    private final MesureIndicateurDetailsRepository repository;

    public MesureIndicateurDetailsBusiness(MesureIndicateurDetailsRepository repository) {
        this.repository = repository;
    }

    public List<MesureIndicateurDetails> getDetailsRelatedDataByNoMesure(Long noMesure) {
        return this.repository.findByMesureIndicateurDetailsIdNoMesure(noMesure);
    }
            
    public List<MesureIndicateurDetails> getDetailsRelatedDataByCodeIndicateur(String codeIndicateur) {
        return this.repository.findByMesureIndicateurDetailsIdCodeIndicateur(codeIndicateur);
    }
            
    public List<MesureIndicateurDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<MesureIndicateurDetails> getMesureIndicateurDetails(Long noMesure, String codeIndicateur) {
        
        MesureIndicateurDetailsId mesureIndicateurDetailsId = new MesureIndicateurDetailsId();
        mesureIndicateurDetailsId.setNoMesure(noMesure);
        mesureIndicateurDetailsId.setCodeIndicateur(codeIndicateur);
            
        return this.repository.findById(mesureIndicateurDetailsId);
    }
    
    public MesureIndicateurDetails save(MesureIndicateurDetails mesureIndicateurDetails) {
        return this.repository.save(mesureIndicateurDetails);
    }
            
    public void delete(MesureIndicateurDetails mesureIndicateurDetails) {
        this.repository.delete(mesureIndicateurDetails);
    }
}
