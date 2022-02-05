/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.InstrumentStructureRemboursement;
import com.progenia.sigdep01_01.data.entity.InstrumentStructureRemboursementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface InstrumentStructureRemboursementRepository extends JpaRepository<InstrumentStructureRemboursement, InstrumentStructureRemboursementId> {
        public List<InstrumentStructureRemboursement> findByInstrumentStructureRemboursementIdNoInstrument(String noInstrument);
        public List<InstrumentStructureRemboursement> findByInstrumentStructureRemboursementIdCodeStructureRemboursement(String codeStructureRemboursement);
}


