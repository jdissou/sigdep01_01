/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.AbonnementService;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.IFacturationAbonnementConsommationSource;
import com.progenia.incubatis01_03.data.entity.IFacturationAbonnementDetailsSource;
import com.progenia.incubatis01_03.data.entity.IFacturationAbonnementPorteurSource;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.AbonnementServiceRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class AbonnementServiceBusiness {
    @Autowired
    private final AbonnementServiceRepository repository;

    public AbonnementServiceBusiness(AbonnementServiceRepository repository) {
        this.repository = repository;
    }

    public Optional<AbonnementService> findById(Long noAbonnement) {
        return this.repository.findById(noAbonnement);
    }

    public List<AbonnementService> findAll() {
        return this.repository.findAll();
    }

    public List<AbonnementService> getAbonnementService(CentreIncubateur centreIncubateur, Porteur porteur) {
        return this.repository.findByCentreIncubateurAndPorteur(centreIncubateur, porteur);
    }

    public List<AbonnementService> getAbonnementServiceEnCours(CentreIncubateur centreIncubateur, Porteur porteur, ServiceFourni serviceFourni) {
        return this.repository.findByIsClotureAndCentreIncubateurAndPorteurAndServiceFourni(false, centreIncubateur, porteur, serviceFourni);
    }

    public List<AbonnementService> getAbonnementServiceEnCours(String codeCentreIncubateur, String noPorteur, String codeService) {
        return this.repository.findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndPorteur_NoPorteurAndServiceFourni_CodeService(false, codeCentreIncubateur, noPorteur, codeService);
    }

    public List<AbonnementService> getAbonnementServiceEnCours(CentreIncubateur centreIncubateur, Porteur porteur) {
        return this.repository.findByIsClotureAndCentreIncubateurAndPorteur(false, centreIncubateur, porteur);
    }

    public List<AbonnementService> getAbonnementServiceEnCoursForPorteur(String codeCentreIncubateur, String noPorteur) {
        return this.repository.findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndPorteur_NoPorteur(false, codeCentreIncubateur, noPorteur);
    }

    public List<AbonnementService> getAbonnementServiceEnCours(CentreIncubateur centreIncubateur, ServiceFourni serviceFourni) {
        return this.repository.findByIsClotureAndCentreIncubateurAndServiceFourni(false, centreIncubateur, serviceFourni);
    }

    public List<AbonnementService> getAbonnementServiceEnCoursForService(String codeCentreIncubateur, String codeService) {
        return this.repository.findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndServiceFourni_CodeService(false, codeCentreIncubateur, codeService);
    }

    public List<IFacturationAbonnementPorteurSource> getFacturationAbonnementPorteurSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation) {
        return this.repository.getFacturationAbonnementPorteurSource(centreIncubateur, sequenceFacturation);
    }

    public List<IFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation) {
        return this.repository.getFacturationAbonnementDetailsSource(centreIncubateur, sequenceFacturation);
    }

    public List<IFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation, Porteur porteur) {
        return this.repository.getFacturationAbonnementDetailsSource(centreIncubateur, sequenceFacturation, porteur);
    }

    public List<IFacturationAbonnementConsommationSource> getFacturationAbonnementConsommationSource(CentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation) {
        return this.repository.getFacturationAbonnementConsommationSource(centreIncubateur, sequenceFacturation);
    }

    public AbonnementService save(AbonnementService abonnementService) {
        return this.repository.save(abonnementService);
    }
            
    public void delete(AbonnementService abonnementService) {
        this.repository.delete(abonnementService);
    }
}
