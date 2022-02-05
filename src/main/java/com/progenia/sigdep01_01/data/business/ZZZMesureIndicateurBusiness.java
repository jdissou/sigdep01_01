/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.MesureIndicateur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.MesureIndicateurRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MesureIndicateurBusiness {
    @Autowired
    private final MesureIndicateurRepository repository;

    public MesureIndicateurBusiness(MesureIndicateurRepository repository) {
        this.repository = repository;
    }

    public Optional<MesureIndicateur> findById(Long noMesure) {
        return this.repository.findById(noMesure);
    }

    public List<MesureIndicateur> findAll() {
        return this.repository.findAll();
    }

    public MesureIndicateur save(MesureIndicateur mesureIndicateur) {
        return this.repository.save(mesureIndicateur);
    }
            
    public void delete(MesureIndicateur mesureIndicateur) {
        this.repository.delete(mesureIndicateur);
    }
    
    public List<MesureIndicateur> getConsultationMesureIndicateurListe(String codeCentreIncubateur, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateMesureBetween(true, codeCentreIncubateur, startDate, endDate);
    }
    
    public List<MesureIndicateur> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(false, codeCentreIncubateur);
    } 
    
    public List<MesureIndicateur> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(true, codeCentreIncubateur);
    } 
}
