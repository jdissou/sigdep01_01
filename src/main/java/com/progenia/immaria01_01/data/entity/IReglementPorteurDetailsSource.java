/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

import com.progenia.immaria01_01.systeme.data.entity.SystemeTypeFacture;
import java.time.LocalDate;

/**
 *
 * @author DELL
 */
public interface IReglementPorteurDetailsSource {
    Long getNoMouvementFacture();
    String getNoChronoFacture(); 
    LocalDate getDateFacture();
    Porteur getPorteur();
    SystemeTypeFacture getTypeFacture();
    Integer getMontantFactureAttendu();
}
