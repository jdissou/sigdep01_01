/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.CategorieLocataire;
import com.progenia.immaria01_01.data.pojo.CategorieLocatairePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface CategorieLocataireRepository extends JpaRepository<CategorieLocataire, String>, CategorieLocataireRepositoryCustom {
    public List<CategorieLocataire> findByCompteClientIsNotNull();
	public List<CategorieLocatairePojo> getReportData();        
}


