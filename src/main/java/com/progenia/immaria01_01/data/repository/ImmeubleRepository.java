/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.Immeuble;
import com.progenia.immaria01_01.data.pojo.ImmeublePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ImmeubleRepository extends JpaRepository<Immeuble, String>, ImmeubleRepositoryCustom {
	public List<ImmeublePojo> getReportData();        
}


