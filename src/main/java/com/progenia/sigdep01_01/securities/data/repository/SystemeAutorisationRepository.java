/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.repository;

import com.progenia.sigdep01_01.securities.data.entity.SystemeAutorisation;
import com.progenia.sigdep01_01.securities.data.entity.SystemeAutorisationId;
import com.progenia.sigdep01_01.securities.data.pojo.AutorisationUtilisateurPojo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @codeCommande Jamâl-Dine DISSOU
 */

@Repository
public interface SystemeAutorisationRepository extends JpaRepository<SystemeAutorisation, SystemeAutorisationId>, SystemeAutorisationRepositoryCustom {
        public List<SystemeAutorisation> findByAutorisationIdCodeCategorieUtilisateur(String codeCategorieUtilisateur);
        public List<SystemeAutorisation> findByAutorisationIdCodeCommande(String codeCommande);
        public List<AutorisationUtilisateurPojo> getAutorisationUtilisateur(String codeUtilisateur, String codeCommande);
}
