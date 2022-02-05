/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

/**
 *
 * @author DELL
 */
public interface ZZZIFacturationAbonnementDetailsSource {
    Instrument getInstrument();
    ServiceFourni getServiceFourni();
    String getNoChronoAbonnement();
    
    /*
        String getNoInstrument();
        String getCodeService();
        String getNoChronoAbonnement();
    */
}
