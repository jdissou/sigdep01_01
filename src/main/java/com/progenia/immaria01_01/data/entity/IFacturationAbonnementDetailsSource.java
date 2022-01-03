/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

/**
 *
 * @author DELL
 */
public interface IFacturationAbonnementDetailsSource {
    Porteur getPorteur();
    ServiceFourni getServiceFourni();
    String getNoChronoAbonnement();
    
    /*
        String getNoPorteur();
        String getCodeService();
        String getNoChronoAbonnement();
    */
}
