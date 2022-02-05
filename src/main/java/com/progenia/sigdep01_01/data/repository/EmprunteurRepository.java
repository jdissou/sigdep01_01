/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.Emprunteur;
import com.progenia.sigdep01_01.data.pojo.EmprunteurPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface EmprunteurRepository extends JpaRepository<Emprunteur, String>, EmprunteurRepositoryCustom {
	public List<EmprunteurPojo> getReportData();
}


