/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.data.repository;

import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.securities.data.pojo.UtilisateurPojo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String>, UtilisateurRepositoryCustom {
        List<Utilisateur> findByLogin(String login);
	public List<UtilisateurPojo> getReportData();        
        
}

