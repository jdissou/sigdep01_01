/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.InstrumentStructureRemboursement;
import com.progenia.sigdep01_01.data.entity.InstrumentStructureRemboursementId;
import com.progenia.sigdep01_01.data.repository.InstrumentStructureRemboursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class InstrumentStructureRemboursementBusiness {
    @Autowired
    private final InstrumentStructureRemboursementRepository repository;

    public InstrumentStructureRemboursementBusiness(InstrumentStructureRemboursementRepository repository) {
        this.repository = repository;
    }

    public List<InstrumentStructureRemboursement> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByInstrumentStructureRemboursementIdNoInstrument(noInstrument);
    }

    public List<InstrumentStructureRemboursement> getDetailsRelatedDataByCodeStructureRemboursement(String codeStructureRemboursement) {
        return this.repository.findByInstrumentStructureRemboursementIdCodeStructureRemboursement(codeStructureRemboursement);
    }

    public List<InstrumentStructureRemboursement> findAll() {
        return this.repository.findAll();
    }

    public Optional<InstrumentStructureRemboursement> findById(InstrumentStructureRemboursementId instrumentStructureRemboursementId) {
        return this.repository.findById(instrumentStructureRemboursementId);
    }

    public InstrumentStructureRemboursement save(InstrumentStructureRemboursement instrumentStructureRemboursement) {
        return this.repository.save(instrumentStructureRemboursement);
    }
            
    public void delete(InstrumentStructureRemboursement instrumentStructureRemboursement) {
        this.repository.delete(instrumentStructureRemboursement);
    }
}
