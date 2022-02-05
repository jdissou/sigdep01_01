/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.InstrumentCommission;
import com.progenia.sigdep01_01.data.entity.InstrumentCommissionId;
import com.progenia.sigdep01_01.data.repository.InstrumentCommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class InstrumentCommissionBusiness {
    @Autowired
    private final InstrumentCommissionRepository repository;

    public InstrumentCommissionBusiness(InstrumentCommissionRepository repository) {
        this.repository = repository;
    }

    public List<InstrumentCommission> getDetailsRelatedDataByNoInstrument(String noInstrument) {
        return this.repository.findByInstrumentCommissionIdNoInstrument(noInstrument);
    }

    public List<InstrumentCommission> getDetailsRelatedDataByCodeCommission(String codeCommission) {
        return this.repository.findByInstrumentCommissionIdCodeCommission(codeCommission);
    }

    public List<InstrumentCommission> findAll() {
        return this.repository.findAll();
    }

    public Optional<InstrumentCommission> findById(InstrumentCommissionId instrumentCommissionId) {
        return this.repository.findById(instrumentCommissionId);
    }

    public InstrumentCommission save(InstrumentCommission instrumentCommission) {
        return this.repository.save(instrumentCommission);
    }
            
    public void delete(InstrumentCommission instrumentCommission) {
        this.repository.delete(instrumentCommission);
    }
}
