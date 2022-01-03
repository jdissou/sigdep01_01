/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.pojo.TrancheValeurPojo;
import com.progenia.immaria01_01.data.entity.TrancheValeur;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface TrancheValeurRepository extends JpaRepository<TrancheValeur, String>, TrancheValeurRepositoryCustom {
	public List<TrancheValeurPojo> getReportData();        
}


