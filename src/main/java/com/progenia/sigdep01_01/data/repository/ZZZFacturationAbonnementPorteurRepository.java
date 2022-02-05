/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationAbonnementInstrument;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementInstrumentRepository extends JpaRepository<ZZZFacturationAbonnementInstrument, ZZZFacturationAbonnementInstrumentId> {
        public List<ZZZFacturationAbonnementInstrument> findByFacturationAbonnementInstrumentIdNoFacturation(Long noFacturation);
        public List<ZZZFacturationAbonnementInstrument> findByFacturationAbonnementInstrumentIdNoInstrument(String noInstrument);

	public List<ZZZFacturationAbonnementInstrument> findByFacturationAbonnementCentreIncubateurAndFacturationAbonnementPeriodeFacturationAndFacturationAbonnementSequenceFacturation(ZZZCentreIncubateur centreIncubateur, IndiceReference periodeFacturation, SequenceFacturation sequenceFacturation);
}
