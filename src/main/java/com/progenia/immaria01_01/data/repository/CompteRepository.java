/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.pojo.ComptePojo;
import com.progenia.immaria01_01.data.entity.Compte;
import com.progenia.immaria01_01.data.pojo.SoldeMouvementComptaPojo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface CompteRepository extends JpaRepository<Compte, String>, CompteRepositoryCustom {
        public List<Compte> findByRegroupementFalse();
        public List<Compte> findByRegroupementFalseAndNoCompteNot(String noCompte);

	public List<SoldeMouvementComptaPojo> getSoldeMouvementCompta(String codeCentreIncubateur, String noCompte, Integer noExercice);        
	public List<ComptePojo> getReportData();        
}
