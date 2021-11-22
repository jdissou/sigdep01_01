/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.pojo.SecteurActivitePojo;
import com.progenia.incubatis01_03.data.entity.DomaineActivite;
import com.progenia.incubatis01_03.data.entity.SecteurActivite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface DomaineActiviteRepository extends JpaRepository<DomaineActivite, String>, DomaineActiviteRepositoryCustom {
        public List<DomaineActivite> findBySecteurActiviteCodeSecteurActivite(String codeSecteurActivite);
	public List<SecteurActivitePojo> getReportData();        
}


