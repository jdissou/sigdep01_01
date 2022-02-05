/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypePaiement;

import java.time.LocalDate;

/**
 *
 * @author DELL
 */
public interface ZZZIReglementInstrumentDetailsSource {
    Long getNoMouvementFacture();
    String getNoChronoFacture(); 
    LocalDate getDateFacture();
    Instrument getInstrument();
    SystemeTypePaiement getTypeFacture();
    Integer getMontantFactureAttendu();
}
