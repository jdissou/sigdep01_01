/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.data.repository;

import com.progenia.immaria01_01.securities.data.pojo.AutorisationUtilisateurPojo;
import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public interface SystemeAutorisationRepositoryCustom {
        public List<AutorisationUtilisateurPojo> getAutorisationUtilisateur(String codeUtilisateur, String codeCommande);  
}
