/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.pojo.IndicateurSuiviPojo;
import com.progenia.incubatis01_03.data.entity.IndicateurSuivi;
import com.progenia.incubatis01_03.data.entity.VariableService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface IndicateurSuiviRepository extends JpaRepository<IndicateurSuivi, String>, IndicateurSuiviRepositoryCustom {
	public List<IndicateurSuiviPojo> getReportData();        
}


