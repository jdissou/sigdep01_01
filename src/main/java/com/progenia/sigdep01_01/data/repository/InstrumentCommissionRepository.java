/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.InstrumentCommission;
import com.progenia.sigdep01_01.data.entity.InstrumentCommissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface InstrumentCommissionRepository extends JpaRepository<InstrumentCommission, InstrumentCommissionId> {
        public List<InstrumentCommission> findByInstrumentCommissionIdNoInstrument(String noInstrument);
        public List<InstrumentCommission> findByInstrumentCommissionIdCodeCommission(String codeCommission);
}


