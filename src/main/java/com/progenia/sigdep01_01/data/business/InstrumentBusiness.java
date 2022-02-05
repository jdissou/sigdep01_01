/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.Instrument;
import com.progenia.sigdep01_01.data.pojo.InstrumentPojo;
import com.progenia.sigdep01_01.data.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class InstrumentBusiness {
    @Autowired
    private final InstrumentRepository repository;

    public InstrumentBusiness(InstrumentRepository repository) {
        this.repository = repository;
    }

    public Optional<Instrument> findById(String codeInstrument) {
        return this.repository.findById(codeInstrument);
    }

    public List<Instrument> findAll() {
        return this.repository.findAll();
    }

    public Instrument save(Instrument instrument) {
        return this.repository.save(instrument);
    }
            
    public void delete(Instrument instrument) {
        this.repository.delete(instrument);
    }
        
    public List<InstrumentPojo> getReportData() {
        return this.repository.getReportData();
    }
}
