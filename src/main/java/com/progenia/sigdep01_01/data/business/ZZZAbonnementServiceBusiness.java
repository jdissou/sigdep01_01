/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZAbonnementService;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.ZZZAbonnementServiceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class AbonnementServiceBusiness {
    @Autowired
    private final ZZZAbonnementServiceRepository repository;

    public AbonnementServiceBusiness(ZZZAbonnementServiceRepository repository) {
        this.repository = repository;
    }

    public Optional<ZZZAbonnementService> findById(Long noAbonnement) {
        return this.repository.findById(noAbonnement);
    }

    public List<ZZZAbonnementService> findAll() {
        return this.repository.findAll();
    }

    public List<ZZZAbonnementService> getAbonnementService(ZZZCentreIncubateur centreIncubateur, Instrument Instrument) {
        return this.repository.findByCentreIncubateurAndInstrument(centreIncubateur, Instrument);
    }

    public List<ZZZAbonnementService> getAbonnementServiceEnCours(ZZZCentreIncubateur centreIncubateur, Instrument Instrument, ServiceFourni serviceFourni) {
        return this.repository.findByIsClotureAndCentreIncubateurAndInstrumentAndServiceFourni(false, centreIncubateur, Instrument, serviceFourni);
    }

    public List<ZZZAbonnementService> getAbonnementServiceEnCours(String codeCentreIncubateur, String noInstrument, String codeService) {
        return this.repository.findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndInstrument_NoInstrumentAndServiceFourni_CodeService(false, codeCentreIncubateur, noInstrument, codeService);
    }

    public List<ZZZAbonnementService> getAbonnementServiceEnCours(ZZZCentreIncubateur centreIncubateur, Instrument Instrument) {
        return this.repository.findByIsClotureAndCentreIncubateurAndInstrument(false, centreIncubateur, Instrument);
    }

    public List<ZZZAbonnementService> getAbonnementServiceEnCoursForInstrument(String codeCentreIncubateur, String noInstrument) {
        return this.repository.findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndInstrument_NoInstrument(false, codeCentreIncubateur, noInstrument);
    }

    public List<ZZZAbonnementService> getAbonnementServiceEnCours(ZZZCentreIncubateur centreIncubateur, ServiceFourni serviceFourni) {
        return this.repository.findByIsClotureAndCentreIncubateurAndServiceFourni(false, centreIncubateur, serviceFourni);
    }

    public List<ZZZAbonnementService> getAbonnementServiceEnCoursForService(String codeCentreIncubateur, String codeService) {
        return this.repository.findByIsClotureAndCentreIncubateur_CodeCentreIncubateurAndServiceFourni_CodeService(false, codeCentreIncubateur, codeService);
    }

    public List<ZZZIFacturationAbonnementInstrumentSource> getFacturationAbonnementInstrumentSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation) {
        return this.repository.getFacturationAbonnementInstrumentSource(centreIncubateur, sequenceFacturation);
    }

    public List<ZZZIFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation) {
        return this.repository.getFacturationAbonnementDetailsSource(centreIncubateur, sequenceFacturation);
    }

    public List<ZZZIFacturationAbonnementDetailsSource> getFacturationAbonnementDetailsSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation, Instrument Instrument) {
        return this.repository.getFacturationAbonnementDetailsSource(centreIncubateur, sequenceFacturation, Instrument);
    }

    public List<ZZZIFacturationAbonnementConsommationSource> getFacturationAbonnementConsommationSource(ZZZCentreIncubateur centreIncubateur, SequenceFacturation sequenceFacturation) {
        return this.repository.getFacturationAbonnementConsommationSource(centreIncubateur, sequenceFacturation);
    }

    public ZZZAbonnementService save(ZZZAbonnementService abonnementService) {
        return this.repository.save(abonnementService);
    }
            
    public void delete(ZZZAbonnementService abonnementService) {
        this.repository.delete(abonnementService);
    }
}
