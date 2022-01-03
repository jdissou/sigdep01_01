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
public interface IFacturationAbonnementConsommationSource {
    Porteur getPorteur();
    ServiceFourni getServiceFourni();
    VariableService getVariableService();

    /*
    String getNoPorteur();
    String getCodeService();
    String getCodeVariable();
    */
}
