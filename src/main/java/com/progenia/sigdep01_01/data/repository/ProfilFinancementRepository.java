/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.ProfilFinancement;
import com.progenia.sigdep01_01.data.pojo.ProfilFinancementPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ProfilFinancementRepository extends JpaRepository<ProfilFinancement, String>, ProfilFinancementRepositoryCustom {
	public List<ProfilFinancementPojo> getReportData();
}


